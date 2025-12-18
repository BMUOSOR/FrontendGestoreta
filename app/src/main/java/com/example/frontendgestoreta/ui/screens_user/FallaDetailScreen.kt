package com.example.frontendgestoreta.ui.screens_user

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontendgestoreta.data.models.MemberDTO
import com.example.frontendgestoreta.viewModel.FallaDetailViewModel

@Composable
fun JoinFallaForm(
    fallaId: Long,
    currentUser: MemberDTO?, // ← Recibe el usuario logueado
    viewModel: FallaDetailViewModel = viewModel(),
    onCancel: () -> Unit   // 👈 nuevo
) {
    // Pre-rellenar nombre y DNI si el usuario está logueado
    val nombre = currentUser?.let { "${it.nombre} ${it.apellidos}".trim() } ?: ""
    val dni = currentUser?.dni ?: ""

    var motivo by remember { mutableStateOf("") }

    val message by viewModel.message.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(message) {
        message?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.messageShown()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Solicitar Inscripción",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )

            // Mostrar datos del usuario
            if (currentUser != null) {
                Text("Usuario: $nombre", fontWeight = FontWeight.Medium)
                Text("DNI: $dni", fontWeight = FontWeight.Medium)
            } else {
                Text("Error: Usuario no identificado", color = MaterialTheme.colorScheme.error)
                return@Column
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = motivo,
                onValueChange = { motivo = it },
                label = { Text("Motivo para unirse") },
                modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp),
                placeholder = { Text("Ej: Quiero participar en la falla porque...") }
            )

            Button(
                onClick = {
                    if (motivo.isBlank()) {
                        viewModel.setMessage("El motivo es obligatorio.")
                        return@Button
                    }
                    viewModel.sendJoinRequest(
                        fallaId = fallaId,
                        nombreCompleto = nombre,
                        dni = dni,
                        motivo = motivo
                    )
                },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                enabled = motivo.isNotBlank()
            ) {
                Text("Enviar Solicitud")
            }
            Text(
                text = "Cancelar",
                modifier = Modifier
                    .weight(1f)
                    .clickable { onCancel() },
                style = MaterialTheme.typography.bodyLarge.copy(
                    textDecoration = TextDecoration.Underline,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}