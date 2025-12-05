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
import com.example.frontendgestoreta.data.models.Tag


@Composable
fun TagSelector(
    tags: List<Tag>,          // Lista de enum Tag
    selectedTag: Tag?,        // Tag seleccionado
    onTagSelected: (Tag?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    // Convertir Tag? a texto visible
    val selectedDescription = selectedTag?.name ?: ""

    // Filtrar según búsqueda
    val filteredTags = remember(searchText, tags) {
        if (searchText.isBlank()) tags
        else tags.filter { it.name.contains(searchText, ignoreCase = true) }
    }

    Column {
        OutlinedTextField(
            value = selectedDescription,
            onValueChange = {},
            label = { Text("Seleccionar etiqueta") },
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

            // Items filtrados
            filteredTags.forEach { tag ->
                DropdownMenuItem(
                    text = { Text(tag.name) },
                    onClick = {
                        onTagSelected(tag)
                        expanded = false
                        searchText = "" // limpiar búsqueda
                    }
                )
            }

            if (filteredTags.isEmpty()) {
                DropdownMenuItem(
                    text = { Text("No se encontraron etiquetas") },
                    onClick = {}
                )
            }
        }
    }
}