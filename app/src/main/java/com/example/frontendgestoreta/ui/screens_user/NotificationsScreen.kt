package com.example.frontendgestoreta.ui.screens_user

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.example.frontendgestoreta.data.models.FallaDTO
import com.example.frontendgestoreta.data.models.MemberDTO
import com.example.frontendgestoreta.viewModel.FallaViewModel
import com.example.frontendgestoreta.viewModel.SuscripcionViewModel

@Composable
fun NotificationsScreen(viewModel: SuscripcionViewModel = viewModel(), member: MemberDTO) {

    viewModel.getFromCuenta(member)
    val fallas by viewModel.fallas.collectAsState()
    var selectedFalla by remember { mutableStateOf<FallaDTO?>(null) }

    Column(Modifier.padding(16.dp)) {
        // NOTIS SWITCH
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Notificaciones activas")
            Switch(
                checked = true, //notificationsOn ?: true,
                onCheckedChange = { newValue ->

                }
            )
        }

        Text("Suscripciones actuales:")
    }

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