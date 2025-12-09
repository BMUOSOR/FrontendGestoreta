package com.example.frontendgestoreta.ui.screens_gestor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
                    .padding(paddingValues) // Importante para el Scaffold
                    .padding(16.dp)
            ) {
                Text(
                    text = "Gestión de la Falla",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botón para alternar entre Miembros y Solicitudes
                Button(
                    onClick = { showRequests = !showRequests },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (showRequests) "Ver Miembros Actuales (${members.size})" else "Ver Solicitudes Pendientes (${requests.size})")
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
                                onCardClick = { selectedMember = member }
                            )
                        }
                    } else {
                        items(requests) { req ->
                            MemberRequestCard(
                                request = req,
                                onCardClick = { selectedMemberRequest = req },
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Detalles de ${member.nombre ?: "Sin nombre"}",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("DNI: ${member.dni ?: "Sin DNI"}")
        Text("Apellidos: ${member.apellidos ?: "Sin Apellidos"}")
        Text("Fecha Nacimiento: ${member.fechaNac ?: "Sin Fecha"}")

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = onBack) {
                Text("Volver")
            }
            Button(
                onClick = { member.idUsuario?.let { onDelete(it) } },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Eliminar Miembro")
            }
        }
    }
}