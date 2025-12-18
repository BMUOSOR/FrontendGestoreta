package com.example.frontendgestoreta.ui.screens_user

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontendgestoreta.R
import com.example.frontendgestoreta.data.models.FallaDTO
import com.example.frontendgestoreta.data.models.MemberDTO
import com.example.frontendgestoreta.viewModel.AuthViewModel
import com.example.frontendgestoreta.viewModel.FallaViewModel
import com.example.frontendgestoreta.viewModel.SuscripcionViewModel

@Composable
fun NotificationsScreen(viewModel: AuthViewModel, member: MemberDTO) {

    var subs = viewModel.suscripciones.collectAsState().value
    var notisActivated by remember { mutableStateOf(true) }

    val textOnEnable = "Notificaciones activas!"
    val textOnDisable = "Notificaciones desactivadas!"
    val duration = Toast.LENGTH_SHORT
    val toastEnable = Toast.makeText(LocalContext.current, textOnEnable, duration)
    val toastDisable = Toast.makeText(LocalContext.current, textOnDisable, duration)

    Log.d("NotificationsScreen", "Subs: ${subs.size}")

    Column(Modifier.padding(24.dp)) {
        // NOTIS SWITCH
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Notificaciones activas",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Switch(
                checked = notisActivated,
                onCheckedChange = {
                    notisActivated = it

                    if (it) {
                        toastEnable.show()
                    } else {
                        toastDisable.show()
                    }


                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color.Black,
                    uncheckedThumbColor = Color.Gray,
                    uncheckedTrackColor = Color.LightGray
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Suscripciones actuales", fontWeight = FontWeight.SemiBold)


        // Box para centrar el texto horizontal y verticalmente
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No estás suscrito a ninguna falla", fontWeight = FontWeight.SemiBold, fontStyle = FontStyle.Italic, color = Color.Gray)
        }

        /*
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(subs.size) { sub ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                   
                }
            }
        }
        */
    }
}