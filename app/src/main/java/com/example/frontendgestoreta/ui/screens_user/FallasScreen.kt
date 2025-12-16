package com.example.frontendgestoreta.ui.screens_user

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontendgestoreta.R
import com.example.frontendgestoreta.data.models.FallaDTO
import com.example.frontendgestoreta.data.models.MemberDTO
import com.example.frontendgestoreta.viewModel.AuthViewModel
import com.example.frontendgestoreta.viewModel.FallaDetailViewModel
import com.example.frontendgestoreta.viewModel.FallaViewModel

@Composable
fun FallasScreen(authViewModel: AuthViewModel, viewModel: FallaViewModel = viewModel()) {
    val fallas by viewModel.fallas.collectAsState()
    // 1. Observamos el estado de carga
    val isLoading by viewModel.isLoading.collectAsState()

    var selectedFallaForForm by remember { mutableStateOf<FallaDTO?>(null) }

    LaunchedEffect(Unit) {
        Log.d("Fallas Screen", "Loading fallas...")
        viewModel.loadFallas()
    }

    Scaffold(
        containerColor = Color(0xFFF8F9FA)
    ) { paddingValues ->

        // 2. Lógica Condicional: ¿Cargando o Contenido?
        if (isLoading) {
            // PANTALLA DE CARGA
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color(0xFF9FA8DA), // Color a juego con tus botones
                    strokeWidth = 4.dp
                )
            }
        } else {
            // CONTENIDO PRINCIPAL (Tu código existente)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                // --- HEADER ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "¡Apúntate",
                            fontFamily = RalewayFont,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 32.sp,
                            color = Color.Black
                        )
                        Text(
                            text = "a una falla!",
                            fontFamily = RalewayFont,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 32.sp,
                            color = Color.Black
                        )
                    }
                    // ICONO DE FILTRO
                    Icon(
                        painter = painterResource(id = R.drawable.ic_map_filter),
                        contentDescription = "Filtros",
                        modifier = Modifier.size(28.dp),
                        tint = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- LISTA DE CARDS ---
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(fallas) { falla ->
                        FallaCardItem(
                            falla = falla,
                            viewModel = viewModel,
                            onJoinClick = { selectedFallaForForm = falla }
                        )
                    }
                }
            }
        }
    }

    // --- POP-UP FORMULARIO ---
    if (selectedFallaForForm != null) {
        AlertDialog(
            onDismissRequest = { selectedFallaForForm = null },
            title = {
                Text(
                    "Solicitar Inscripción",
                    fontFamily = RalewayFont,
                    fontWeight = FontWeight.SemiBold
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val currentUser by authViewModel.currentUser.collectAsState()
                    JoinFallaFormContent(
                        fallaId = selectedFallaForForm!!.idFalla ?: 0L,
                        currentUser = currentUser,
                        onDismiss = { selectedFallaForForm = null }
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedFallaForForm = null }) {
                    Text("Cancelar", fontFamily = RalewayFont, fontWeight = FontWeight.Light)
                }
            },
            confirmButton = {},
            properties = DialogProperties(dismissOnClickOutside = false),
            containerColor = Color.White
        )
    }
}

// El resto de componentes (FallaCardItem y JoinFallaFormContent) se mantienen EXACTAMENTE igual que en tu código original.
// No hace falta repetirlos aquí si ya los tienes en tu archivo,
// pero asegúrate de que FallaCardItem sigue usando la lógica segura (toIntOrNull) que corregimos antes.

@Composable
fun FallaCardItem(
    falla: FallaDTO,
    viewModel: FallaViewModel,
    onJoinClick: () -> Unit
) {
    val randomPrice = remember {
        val base = (300..500).random()
        val lastDigitOptions = listOf(9, 5, 0)
        val chosenLastDigit = lastDigitOptions.random()
        base - (base % 10) + chosenLastDigit
    }
    val randomMembers = remember { (45..500).random() }
    val randomYear = remember { (1880..1994).random() }
    var memberCount by remember { mutableStateOf(0) }
    // Lógica segura de ID
    val escudoResId = remember(falla.escudo) {
        falla.escudo?.toIntOrNull()
    }

    LaunchedEffect(falla.idFalla) {
        falla.idFalla?.let { id ->
            viewModel.getMemberCount(id) { count -> memberCount = count }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Título
            Text(
                text = falla.nombre ?: "Falla Sin Nombre",
                fontFamily = RalewayFont,
                fontWeight = FontWeight.SemiBold,
                fontSize = 22.sp,
                color = Color.Black
            )

            // Email
            val fakeEmail = "falla@${falla.nombre?.lowercase()?.replace(" ", "") ?: "gestoreta"}.com"
            Text(
                text = fakeEmail,
                fontFamily = RalewayFont,
                fontWeight = FontWeight.Light,
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Estadísticas
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // MIEMBROS
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_member_icon_falla),
                        contentDescription = "Miembros",
                        modifier = Modifier.size(20.dp),
                        tint = Color.Black
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$randomMembers",
                        fontFamily = RalewayFont,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                }

                // PRECIO
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_precio_fallas),
                        contentDescription = "Precio",
                        modifier = Modifier.size(18.dp),
                        tint = Color.Black
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$randomPrice€ / año",
                        fontFamily = RalewayFont,
                        fontWeight = FontWeight.Light,
                        fontSize = 14.sp
                    )
                }

                // ANTIGÜEDAD
                Text(
                    text = "desde $randomYear",
                    fontFamily = RalewayFont,
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Dirección
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = falla.direccion ?: "Calle desconocida",
                    fontFamily = RalewayFont,
                    fontWeight = FontWeight.Light,
                    fontSize = 13.sp,
                    color = Color.Gray,
                    modifier = Modifier.weight(0.6f),
                    lineHeight = 16.sp
                )

                Text(
                    text = "963312629",
                    fontFamily = RalewayFont,
                    fontWeight = FontWeight.Light,
                    fontSize = 13.sp,
                    color = Color.Gray,
                    modifier = Modifier.weight(0.4f).wrapContentWidth(Alignment.End)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Imagen + Botón
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (escudoResId != null) {
                    Image(
                        painter = painterResource(id = escudoResId),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(Color.LightGray, RoundedCornerShape(8.dp))
                    )
                }

                Button(
                    onClick = onJoinClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9FA8DA)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .height(45.dp)
                        .width(160.dp)
                ) {
                    Text(
                        text = "Inscríbete",
                        fontFamily = RalewayFont,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun JoinFallaFormContent(
    fallaId: Long,
    currentUser: MemberDTO?,
    onDismiss: () -> Unit,
    viewModel: FallaDetailViewModel = viewModel()
) {
    val nombre = currentUser?.let { "${it.nombre} ${it.apellidos}".trim() } ?: ""
    val dni = currentUser?.dni ?: ""
    var motivo by remember { mutableStateOf("") }
    val message by viewModel.message.collectAsState()

    LaunchedEffect(message) {
        if (message?.contains("éxito") == true) {
            onDismiss()
            viewModel.messageShown()
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (currentUser == null) {
            Text("Error: No estás logueado", color = MaterialTheme.colorScheme.error, fontFamily = RalewayFont, fontWeight = FontWeight.SemiBold)
            return@Column
        }

        Text("Usuario: $nombre", fontFamily = RalewayFont, fontWeight = FontWeight.SemiBold)
        Text("DNI: $dni", fontFamily = RalewayFont, fontWeight = FontWeight.Light)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = motivo,
            onValueChange = { motivo = it },
            label = { Text("Motivo para unirse", fontFamily = RalewayFont, fontWeight = FontWeight.Light) },
            modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp),
            placeholder = { Text("Ej: Quiero participar porque...", fontFamily = RalewayFont, fontWeight = FontWeight.Light) }
        )

        Button(
            onClick = {
                if (motivo.isBlank()) {
                    viewModel.setMessage("El motivo es obligatorio.")
                    return@Button
                }
                viewModel.sendJoinRequest(fallaId, nombre, dni, motivo)
            },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            enabled = motivo.isNotBlank(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9FA8DA))
        ) {
            Text("Enviar Solicitud", fontFamily = RalewayFont, fontWeight = FontWeight.SemiBold)
        }

        if (message != null && !message!!.contains("éxito")) {
            Text(message ?: "", color = Color.Red, fontSize = 12.sp, fontFamily = RalewayFont, fontWeight = FontWeight.Light)
        }
    }
}