package com.example.frontendgestoreta.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontendgestoreta.data.models.MemberDTO
import com.example.frontendgestoreta.data.models.MemberRequestDTO

// --- 1. Componente para la tarjeta de un Miembro Actual ---
@Composable
fun MemberCard(
    member: MemberDTO,
    onCardClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onCardClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = member.nombre ?: "Sin nombre",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = member.dni ?: "Sin dni",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

// --- 2. Componente para la tarjeta de una Solicitud Pendiente ---
@Composable
fun MemberRequestCard(
    request: MemberRequestDTO,
    onCardClick: () -> Unit,
    onAcceptClick: () -> Unit,
    onRejectClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onCardClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = request.contenido ?: "Sin contenido",
                fontWeight = FontWeight.Bold
            )
            Text(
                text = request.motivo ?: "Sin motivo",
                style = MaterialTheme.typography.bodyMedium
            )

            // Botones de acción
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = onAcceptClick) {
                    Text("Aceptar")
                }
                OutlinedButton(onClick = onRejectClick) {
                    Text("Rechazar")
                }
            }
        }
    }
}