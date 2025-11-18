package com.example.frontendgestoreta.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.frontendgestoreta.data.models.FallaDTO

@Composable
fun FallaSelector(
    fallas: List<FallaDTO>,
    selectedFallaId: Long?,
    onFallaSelected: (Long?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    val filteredFallas = remember(searchText, fallas) {
        if (searchText.isBlank()) fallas
        else fallas.filter { it.nombre.contains(searchText, ignoreCase = true) }
    }

    Column {
        OutlinedTextField(
            value = selectedFallaId?.let { id -> fallas.find { it.idFalla == id }?.nombre } ?: "",
            onValueChange = {},
            label = { Text("Seleccionar falla") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true },
            enabled = false
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {

            // Barra de búsqueda dentro del menú
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Buscar") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            // Lista de fallas
            filteredFallas.forEach { falla ->
                DropdownMenuItem(
                    text = { Text(falla.nombre) },
                    onClick = {
                        onFallaSelected(falla.idFalla)
                        expanded = false
                    }
                )
            }

            if (filteredFallas.isEmpty()) {
                DropdownMenuItem(
                    text = { Text("No se encontraron fallas") },
                    onClick = { }
                )
            }
        }
    }
}
