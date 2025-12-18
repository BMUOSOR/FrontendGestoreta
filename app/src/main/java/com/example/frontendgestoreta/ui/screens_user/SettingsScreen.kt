package com.example.frontendgestoreta.ui.screens_user

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.frontendgestoreta.R
import com.example.frontendgestoreta.navigation.AppScreens
import com.example.frontendgestoreta.ui.components.SettingsItem

@Composable
fun SettingsScreen(navController: NavHostController) {
    val context = LocalContext.current

    // ESTADO PARA EL SLIDE/TICKET
    var showTicket by remember { mutableStateOf(false) }

    // Datos del usuario (esto debería venir de tu AuthViewModel habitualmente)
    val currentUserName = "Francisco Perez Llopis"

    // Scaffold nos ayuda a que el BottomSheet se superponga correctamente
    Scaffold(
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "Ajustes",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Ítem de Cuenta
            SettingsItem(
                leftImagePainter = painterResource(id = R.drawable.ic_g_cuenta),
                title = "Cuenta",
                description = "Modificar datos de usuario",
                onClick = {
                    navController.navigate(AppScreens.ModifyUserScreen.route)
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Ítem de Notificaciones
            SettingsItem(
                leftImagePainter = painterResource(id = R.drawable.ic_casales),
                title = "Notificaciones",
                description = "Configuración de notificaciones",
                onClick = {
                    navController.navigate(AppScreens.NotificationsScreen.route)
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            SettingsItem(
                leftImagePainter = painterResource(id = R.drawable.ic_member_settings_ticket),
                title = "Código de miembro",
                description = "Ver tu código único de usuario",
                onClick = {
                    showTicket = true // Activa el slide
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            TextButton(
                modifier = Modifier.fillMaxWidth().height(48.dp),
                onClick = { restartApp(context) }
            ) {
                Text(
                    "Cerrar sesión",
                    textDecoration = TextDecoration.Underline,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // LÓGICA PARA MOSTRAR EL TICKET
        if (showTicket) {
            TicketScreen(
                userName = currentUserName,
                onDismiss = { showTicket = false }
            )
        }
    }
}

fun restartApp(context: Context) {
    val packageManager = context.packageManager
    val intent = packageManager.getLaunchIntentForPackage(context.packageName)
    val componentName = intent!!.component
    val mainIntent = Intent.makeRestartActivityTask(componentName)
    context.startActivity(mainIntent)
    Runtime.getRuntime().exit(0)
}