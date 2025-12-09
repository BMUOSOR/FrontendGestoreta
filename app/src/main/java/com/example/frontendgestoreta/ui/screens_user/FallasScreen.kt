package com.example.frontendgestoreta.ui.screens_user

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontendgestoreta.data.models.FallaDTO
import com.example.frontendgestoreta.data.models.MemberDTO
import com.example.frontendgestoreta.viewModel.AuthViewModel
import com.example.frontendgestoreta.viewModel.FallaViewModel
import com.example.frontendgestoreta.viewModel.FallaDetailViewModel // Importar el ViewModel del formulario


@Composable
fun FallasScreen(authViewModel: AuthViewModel, viewModel: FallaViewModel = viewModel()) {
    val fallas by viewModel.fallas.collectAsState()

    var selectedFalla by remember { mutableStateOf<FallaDTO?>(null) }
    // Nuevo estado para controlar la visibilidad del pop-up del formulario
    var showJoinForm by remember { mutableStateOf(false) }

    Log.d("Fallas Screen", "Loading fallas...")
    LaunchedEffect(Unit) { viewModel.loadFallas() }

    // --- Contenido Principal ---
    if (selectedFalla != null) {
        // PANTALLA DE DETALLE DE FALLA
        FallaDetailScreen(
            falla = selectedFalla!!,
            onBack = { selectedFalla = null },
            // El clic de "Solicitar Inscripción" ahora solo abre el pop-up
            onJoinClick = { showJoinForm = true }
        )

        // *** Lógica del Pop-up (AlertDialog) ***
        // Dentro de FallasScreen, donde tienes el AlertDialog
        if (showJoinForm) {
            AlertDialog(
                onDismissRequest = { showJoinForm = false },
                title = { Text("Solicitar Inscripción") },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // ← PASA currentUser AQUI
                        val currentUser by authViewModel.currentUser.collectAsState()

                        JoinFallaFormContent(
                            fallaId = selectedFalla!!.idFalla,
                            currentUser = currentUser,
                            onDismiss = { showJoinForm = false }
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showJoinForm = false }) {
                        Text("Cancelar")
                    }
                },
                confirmButton = {},
                properties = DialogProperties(dismissOnClickOutside = false)
            )
        }
    } else {
        // PANTALLA DE LISTA
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Fallas actuales",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Fallas actuales", fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(fallas) { falla ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedFalla = falla },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = falla.nombre ?: "Sin nombre", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text(text = falla.direccion ?: "Sin dirección", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FallaDetailScreen(
    falla: FallaDTO,
    onBack: () -> Unit,
    onJoinClick: () -> Unit // Callback para abrir el formulario
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "Detalles de la Falla",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Información organizada
        DetailItem("Nombre", falla.nombre ?: "Sin nombre")
        Spacer(modifier = Modifier.height(16.dp))
        DetailItem("Dirección", falla.direccion ?: "Sin dirección")
        Spacer(modifier = Modifier.height(16.dp))
        DetailItem("Fecha de creación", falla.fechaCreacion.toString() ?: "Sin fecha")
        Spacer(modifier = Modifier.height(20.dp))

        // Botón Apuntarse (abre el pop-up)
        Button(
            onClick = onJoinClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Solicitar Inscripción", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.weight(1f))

        // Botón Volver
        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
        ) {
            Text("Volver a la lista", fontSize = 16.sp)
        }
    }
}

@Composable
fun DetailItem(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun JoinFallaFormContent(
    fallaId: Long,
    currentUser: MemberDTO?,
    onDismiss: () -> Unit,
    viewModel: FallaDetailViewModel = viewModel()
) {
    // Pre-rellenar con datos del usuario logueado
    val nombre = currentUser?.let { "${it.nombre} ${it.apellidos}".trim() } ?: ""
    val dni = currentUser?.dni ?: ""

    var motivo by remember { mutableStateOf("") }

    val message by viewModel.message.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(message) {
        message?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.messageShown()
            if (it.contains("éxito")) {
                onDismiss()
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (currentUser == null) {
            Text("Error: No estás logueado", color = MaterialTheme.colorScheme.error)
            return@Column
        }

        Text("Usuario: $nombre", fontWeight = FontWeight.Medium)
        Text("DNI: $dni", fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = motivo,
            onValueChange = { motivo = it },
            label = { Text("Motivo para unirse") },
            modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp),
            placeholder = { Text("Ej: Quiero participar porque...") }
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
            enabled = motivo.isNotBlank()
        ) {
            Text("Enviar Solicitud")
        }
    }
}