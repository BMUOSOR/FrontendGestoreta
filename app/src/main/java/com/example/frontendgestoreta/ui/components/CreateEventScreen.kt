package com.example.frontendgestoreta.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontendgestoreta.data.api.RetrofitClient
import com.example.frontendgestoreta.data.models.EventDTO
import com.example.frontendgestoreta.repository.EventRepository
import com.example.frontendgestoreta.viewModel.EventViewModel

@Composable
fun CreateEventScreen(
    onBack: () -> Unit,
    viewModel: EventViewModel = viewModel()
) {
    val eventRepository : EventRepository = EventRepository(RetrofitClient.apiService)
    // Estados para los campos del formulario
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var ubicacion by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Crear Nuevo Evento",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Campo Título
        Text(
            text = "Título",
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Ingresa el título del evento") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo Descripción
        Text(
            text = "Descripción",
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(4.dp))

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            placeholder = { Text("Describe el evento") },
            singleLine = false,
            maxLines = 4
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Ubicación",
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(4.dp))

        OutlinedTextField(
            value = ubicacion,
            onValueChange = { ubicacion = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("¿Dónde será el evento?") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        Spacer(modifier = Modifier.height(24.dp))

        // Botones de acción
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = { onBack() }
            ) {
                Text("Cancelar")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    // Lógica para crear evento con los datos
                    val nuevoEvento = EventDTO(
                        titulo = titulo,
                        descripcion = descripcion,
                        ubicacion = ubicacion
                    )
                    Log.d("CreateEventScreen", "Evento creado: " + nuevoEvento.titulo)
                    viewModel.createEvent(nuevoEvento);
                    onBack()
                },
                enabled = titulo.isNotBlank() && descripcion.isNotBlank() && ubicacion.isNotBlank()
            ) {
                Text("Crear Evento")
            }
        }
    }
}