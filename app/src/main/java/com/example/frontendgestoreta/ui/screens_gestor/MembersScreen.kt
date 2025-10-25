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

// Modelo temporal hasta tener el DTO real del backend
data class Member(
    val id: Long,
    val nombre: String,
    val cargo: String
)

@Composable
fun MembersScreen() {
    // Lista temporal de prueba
    val members = remember {
        listOf(
            Member(1, "María López", "Presidenta"),
            Member(2, "Carlos Pérez", "Secretario"),
            Member(3, "Ana García", "Tesorera")
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Text(
            text = "Miembros de la Falla",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(members) { member ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = member.nombre, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(text = member.cargo, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
