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
import com.example.frontendgestoreta.data.models.TagDTO
@Composable
fun TagSelector(
    tags: List<TagDTO>,
    selectedTagId: Long?,
    onTagSelected: (Long?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    // Estado local para mostrar la descripción seleccionada
    var selectedDescription by remember(selectedTagId, tags) {
        mutableStateOf(
            selectedTagId?.let { id -> tags.find { it.idTag == id }?.description ?: "Etiqueta ${id}" } ?: ""
        )
    }

    val filteredTags = remember(searchText, tags) {
        if (searchText.isBlank()) tags
        else tags.filter { it.description.contains(searchText, ignoreCase = true) == true }
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
            // Barra de búsqueda
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Buscar") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            // Lista de tags
            filteredTags.forEach { tag ->
                DropdownMenuItem(
                    text = { Text(tag.description.orEmpty()) },
                    onClick = {
                        selectedDescription = tag.description.orEmpty() // Actualiza el campo
                        onTagSelected(tag.idTag)                       // Actualiza DTO
                        expanded = false
                    }
                )
            }

            if (filteredTags.isEmpty()) {
                DropdownMenuItem(
                    text = { Text("No se encontraron etiquetas") },
                    onClick = { }
                )
            }
        }
    }
}