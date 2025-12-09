package com.example.frontendgestoreta.ui.screens_user

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontendgestoreta.data.models.EventDTO
import com.example.frontendgestoreta.data.models.MemberDTO
import com.example.frontendgestoreta.viewModel.EventViewModel
import com.example.frontendgestoreta.viewModel.MemberViewModel
import java.time.LocalDate

@Composable
fun ModifyUserScreen(
    onBack: () -> Unit,
    member: MemberDTO,
    viewModel: MemberViewModel = viewModel()
) {
    // Estados para los campos del formulario
    var nombre by remember { mutableStateOf(member.nombre ?: "") }
    var apellidos by remember { mutableStateOf(member.apellidos ?: "") }
    var dni by remember { mutableStateOf(member.dni ?: "") }


    val scrollState = rememberScrollState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.Start
    ) {
        // Campo Nombre
        Text(
            text = "Nombre",
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Ingresa tu nombre") },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Campo Apellidos
        Text(
            text = "Apellidos",
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = apellidos,
            onValueChange = { apellidos = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Ingresa tus apellidos") },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Campo DNI
        Text(
            text = "DNI",
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )



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
                    val editedMember = member.copy(
                        nombre = nombre,
                    )
                    viewModel.updateUsuario(editedMember);
                    onBack()
                },
                enabled = nombre.isNotBlank() && apellidos.isNotBlank()
            ) {
                Text("Guardar Cambios")
            }
        }
    }
}