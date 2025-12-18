package com.example.frontendgestoreta.ui.screens_gestor

import android.R.attr.background
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontendgestoreta.data.models.MemberDTO
import com.example.frontendgestoreta.data.models.MemberRequestDTO
import com.example.frontendgestoreta.viewModel.MemberViewModel
import com.example.frontendgestoreta.ui.components.MemberCard
import com.example.frontendgestoreta.ui.components.MemberRequestCard
import com.example.frontendgestoreta.ui.components.MemberDetailScreen
import com.example.frontendgestoreta.ui.components.MemberRequestDetailScreen
import com.example.frontendgestoreta.R
import com.example.frontendgestoreta.ui.screens_user.RalewayFont

@Composable
fun MembersScreen(viewModel: MemberViewModel = viewModel()) {
    val members by viewModel.members.collectAsState()
    val requests by viewModel.requests.collectAsState()
    val message by viewModel.message.collectAsState() // Recoger el mensaje/error

    var selectedMember by remember { mutableStateOf<MemberDTO?>(null) }
    var selectedMemberRequest by remember { mutableStateOf<MemberRequestDTO?>(null) }
    var showRequests by remember { mutableStateOf(false) }


    val snackbarHostState = remember { SnackbarHostState() }


    LaunchedEffect(Unit) {
        viewModel.loadMembers()
    }

    LaunchedEffect(message) {
        message?.let {
            snackbarHostState.showSnackbar(it)
        }
    }


    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        if (selectedMember != null) {
            MemberDetailScreenWithDelete(
                member = selectedMember!!,
                onBack = { selectedMember = null },
                onDelete = {
                    selectedMember?.let { member ->
                        viewModel.deleteMember(member)
                    }
                    selectedMember = null
                }
            )
        } else if (selectedMemberRequest != null) {
            // Pantalla de detalle de Solicitud (sin cambios, solo ver)
            MemberRequestDetailScreen(request = selectedMemberRequest!!) {
                selectedMemberRequest = null
            }
        } else {
            // Pantalla Principal (Lista)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(R.color.white))
                    .padding(paddingValues) // Importante para el Scaffold
                    .padding(16.dp)
            ) {
                Text(
                    text = "Miembros",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp) // menos padding abajo
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botón para alternar entre Miembros y Solicitudes
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(colorResource(R.color.lillac)),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // ACEPTADOS
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(4.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (!showRequests) colorResource(R.color.white) else Color.Transparent)
                            .clickable { showRequests = false },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Aceptados", color = Color.Black)
                    }

                    // SOLICITUDES
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(4.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (showRequests) colorResource(R.color.white) else Color.Transparent)
                            .clickable { showRequests = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Solicitudes", color = Color.Black)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (showRequests) "Solicitudes Pendientes" else "Miembros Actuales",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Lista dinámica (LazyColumn)
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    if (!showRequests) {
                        items(members) { member ->
                            MemberCard(
                                member = member,
                                onButtonClick = { selectedMember = member }
                            )
                        }
                    } else {
                        items(requests) { req ->
                            MemberRequestCard(
                                request = req,
                                onInfoClick = { selectedMemberRequest = req },
                                // Llama a las funciones del ViewModel
                                onAcceptClick = { viewModel.acceptRequest(req.idSolicitud!!) },
                                onRejectClick = { viewModel.rejectRequest(req.idSolicitud!!) }
                            )
                        }
                    }
                }
            }
        }
    }
}

// --- Componente de Detalle de Miembro MODIFICADO para incluir el botón de Eliminar ---
@Composable
fun MemberDetailScreenWithDelete(member: MemberDTO, onBack: () -> Unit, onDelete: (Long) -> Unit) {
    val (showInfoDialog, setShowInfoDialog) = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.white))
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Detalles de ${member.nombre ?: "Sin nombre"}",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.black)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("DNI: ${member.dni ?: "Sin DNI"}",color = colorResource(R.color.black))
        Text("Apellidos: ${member.apellidos ?: "Sin Apellidos"}",color = colorResource(R.color.black))
        Text("Fecha Nacimiento: ${member.fechaNac ?: "Sin Fecha"}",color = colorResource(R.color.black))

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = onBack
            ) {
                Text(text ="Volver",
                    textDecoration = TextDecoration.Underline
                )
            }
            Button(
                onClick = {
                    member.idUsuario?.let { onDelete(it) }
                    setShowInfoDialog(true)
                          },
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.purple_200))
            ) {
                Text("Eliminar Miembro")
            }
            if (showInfoDialog) {
                AlertDialog(
                    onDismissRequest = { setShowInfoDialog(false) },
                    containerColor = colorResource(R.color.white),
                    title = { Text("Usuario modificado", color = colorResource(R.color.black), fontFamily = RalewayFont, fontWeight = FontWeight.SemiBold) },
                    text = { Text("Se ha actualizado la información del usuario", color = colorResource(R.color.black), fontFamily = RalewayFont) },
                    confirmButton = {
                        TextButton(onClick = {
                            setShowInfoDialog(false)
                            onBack()
                        }) {
                            Text("Aceptar", color = colorResource(R.color.indigo), fontFamily = RalewayFont)
                        }
                    }
                )
            }
        }
    }
}