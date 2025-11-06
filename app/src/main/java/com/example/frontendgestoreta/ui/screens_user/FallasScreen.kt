package com.example.frontendgestoreta.ui.screens_user

import android.util.Log
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
import com.example.frontendgestoreta.data.models.FallaDTO
import com.example.frontendgestoreta.viewModel.FallaViewModel


@Composable
fun FallasScreen(viewModel: FallaViewModel = viewModel()) {
    val fallas by viewModel.fallas.collectAsState()

    var selectedFalla by remember { mutableStateOf<FallaDTO?>(null) }

    Log.d("Fallas Screen", "Loading fallas...")
    LaunchedEffect(Unit) { viewModel.loadFallas() }

    if (selectedFalla != null) {
        FallaDetailScreen(falla = selectedFalla!!) {
            selectedFalla = null
        }
    } else {
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

            // la lista de fallas
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
fun FallaDetailScreen(falla: FallaDTO, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Detalles de ${falla.nombre ?: "Sin nombre"}",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Dirección: ${falla.direccion ?:"Sin Dirección"}")
        Spacer(modifier = Modifier.height(16.dp))
        Text("Fecha de creación: ${falla.fechaCreacion ?:"Sin Fecha de creación"}")
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onBack) {
            Text("Volver")
        }
    }
}
