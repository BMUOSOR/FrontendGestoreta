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

data class Member(
    val id: Long,
    val nombre: String,
    val cargo: String
)

data class MemberRequest(
    val id: Long,
    val nombre: String,
    val motivo: String
)

@Composable
fun MembersScreen() {
    // Datos de prueba (más adelante vendrán del backend)
    val members = remember {
        listOf(
            Member(1, "María López", "Presidenta"),
            Member(2, "Carlos Pérez", "Secretario"),
            Member(3, "Ana García", "Tesorera"),
            Member(4, "Carla Romero", "Fallera"),
        )
    }

    val requests = remember {
        listOf(
            MemberRequest(10, "Lucía Torres", "Quiere unirse a la falla"),
            MemberRequest(11, "Javier Díaz", "Quiere ser fallero infantil")
        )
    }

    // Estado para el miembro seleccionado
    var selectedMember by remember { mutableStateOf<Member?>(null) }
    var selectedMemberRequest by remember{ mutableStateOf<MemberRequest?>(null)}
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
                            Text(text = member.nombre, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text(text = member.cargo, style = MaterialTheme.typography.bodyMedium)
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
                            Text(text = req.nombre, fontWeight = FontWeight.Bold)
                            Text(text = req.motivo, style = MaterialTheme.typography.bodyMedium)
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
fun MemberDetailScreen(member: Member, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Detalles de ${member.nombre}",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Cargo: ${member.cargo}")
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onBack) {
            Text("Volver")
        }
    }
}
@Composable
fun MemberRequestDetailScreen(request: MemberRequest, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Detalles de ${request.nombre}",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Cargo: ${request.motivo}")
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onBack) {
            Text("Volver")
        }
    }
}