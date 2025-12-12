package com.example.frontendgestoreta.ui.screens_user

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.frontendgestoreta.R
import com.example.frontendgestoreta.navigation.AppScreens
import com.example.frontendgestoreta.ui.components.CreateEventScreen
import com.example.frontendgestoreta.ui.components.SettingsItem
import com.example.frontendgestoreta.viewModel.AuthViewModel
import com.example.frontendgestoreta.viewModel.EventViewModel


@Composable
fun SettingsScreen(navController: NavHostController) {

    val context = LocalContext.current

    Column(
        modifier = Modifier.background(Color.White)
    ) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color.White)
        ) {

            Text(
                text = "Ajustes",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            SettingsItem(
                leftImagePainter = painterResource(id = R.drawable.ic_g_cuenta),
                title = "Cuenta",
                description = "Modificar datos de usuario",
                onClick = {
                    navController.navigate(AppScreens.ModifyUserScreen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true // Avoid multiple copies of the same screen
                        restoreState = true // If the user was in the middle of something
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            SettingsItem(
                leftImagePainter = painterResource(id = R.drawable.ic_casales),
                title = "Notificaciones",
                description = "Configuración de las notificaciones recibidas por suscripciones",
                onClick = {
                    navController.navigate(AppScreens.NotificationsScreen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true // Avoid multiple copies of the same screen
                        restoreState = true // If the user was in the middle of something
                    }
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            TextButton(
                modifier = Modifier.fillMaxWidth()
                    .height(48.dp),
                onClick = {
                    restartApp(context)
                }
            ) {
                Text(
                    "Cerrar sesión",
                    textDecoration = TextDecoration.Underline,
                    color = colorResource(R.color.black)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

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