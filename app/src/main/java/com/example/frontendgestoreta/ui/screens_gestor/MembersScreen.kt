package com.example.frontendgestoreta.ui.screens_gestor

import androidx.compose.foundation.clickable
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
import com.example.frontendgestoreta.viewmodel.MembersViewModel



@Composable
fun MembersScreen(viewModel: MembersViewModel = viewModel()) {
    val members by viewModel.members.collectAsState()
    val requests by viewModel.requests.collectAsState()

    var selectedMember by remember { mutableStateOf<MemberDTO?>(null) }
    var selectedMemberRequest by remember { mutableStateOf<MemberRequestDTO?>(null) }

    LaunchedEffect(Unit) { viewModel.loadMembers() }

    // Si hay un miembro seleccionado muestra su info
    if (selectedMember != null) {
        MemberDetailScreen(member = selectedMember!!) {
            selectedMember = null
        }
    } else if (selectedMemberRequest != null) {
        MemberRequestDetailScreen(request = selectedMemberRequest!!) {
            selectedMemberRequest = null
        }
    } else {
        // la pantalla principal con las dos listas
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Miembros de la Falla",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // la lista de miembros
            Text("Miembros actuales", fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(members) { member ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedMember = member },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = member.nombre ?: "Sin nombre", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text(text = member.dni ?: "Sin dni", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // la lista de solicitudes
            Text("Solicitudes pendientes", fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(requests) { req ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedMemberRequest = req }, // <-- esto faltaba
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = req.contenido ?: "Sin contenido", fontWeight = FontWeight.Bold)
                            Text(text = req.motivo ?: "Sin motivo", style = MaterialTheme.typography.bodyMedium)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Button(onClick = { /* aprobar solicitud */ }) {
                                    Text("Aceptar")
                                }
                                OutlinedButton(onClick = { /* rechazar solicitud */ }) {
                                    Text("Rechazar")
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun MemberDetailScreen(member: MemberDTO, onBack: () -> Unit) {
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
        Text("DNI: ${member.dni ?:"Sin DNI"}")
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onBack) {
            Text("Volver")
        }
    }
}
@Composable
fun MemberRequestDetailScreen(request: MemberRequestDTO, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Detalles de ${request.contenido ?: "Sin contenido"}",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Cargo: ${request.motivo ?: "Sin motivo"}")
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onBack) {
            Text("Volver")
        }
    }
}