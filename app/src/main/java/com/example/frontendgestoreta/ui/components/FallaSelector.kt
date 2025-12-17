package com.example.frontendgestoreta.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.frontendgestoreta.R
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
            label = { Text("Seleccionar falla", color = colorResource(R.color.black)) },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
                .border(2.dp,colorResource(R.color.black), RoundedCornerShape(12.dp)),
            enabled = false,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = colorResource(R.color.white),
                unfocusedContainerColor = colorResource(R.color.white),
                unfocusedPlaceholderColor = colorResource(R.color.white),
                focusedTextColor = colorResource(R.color.white)
            )
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth().background(colorResource(R.color.white))
        ) {

            // Barra de búsqueda dentro del menú
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Buscar", color = colorResource(R.color.black)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            // Lista de fallas
            filteredFallas.forEach { falla ->
                DropdownMenuItem(
                    text = { Text(falla.nombre, color = colorResource(R.color.black)) },
                    onClick = {
                        onFallaSelected(falla.idFalla)
                        expanded = false
                    }
                )
            }

            if (filteredFallas.isEmpty()) {
                DropdownMenuItem(
                    text = { Text("No se encontraron fallas", color = colorResource(R.color.black)) },
                    onClick = { }
                )
            }
        }
    }
}
