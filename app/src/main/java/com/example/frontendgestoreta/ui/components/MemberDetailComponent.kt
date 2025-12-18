 package com.example.frontendgestoreta.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontendgestoreta.R
import com.example.frontendgestoreta.data.models.MemberDTO
import com.example.frontendgestoreta.data.models.MemberRequestDTO


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
        Text("DNI: ${member.dni ?: "Sin DNI"}")
        // Aquí podrías añadir más campos relevantes de MemberDTO
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onBack) {
            Text("Volver")
        }
    }
}

// --- Componente de Detalle de Solicitud de Miembro ---
@Composable
fun MemberRequestDetailScreen(request: MemberRequestDTO, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.white))
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Detalles de Solicitud",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.black)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text="Contenido: ${request.contenido ?: "Sin contenido"}", color = colorResource(R.color.black))
        Text(text="Motivo: ${request.motivo ?: "Sin motivo"}", color = colorResource(R.color.black))
        // Aquí podrías añadir más campos relevantes de MemberRequestDTO
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onBack) {
            Text("Volver",textDecoration = TextDecoration.Underline)
        }
    }
}