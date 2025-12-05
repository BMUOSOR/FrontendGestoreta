package com.example.frontendgestoreta.ui.screens_user

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.frontendgestoreta.navigation.AppScreens
import com.example.frontendgestoreta.ui.components.CreateEventScreen
import com.example.frontendgestoreta.viewModel.AuthViewModel
import com.example.frontendgestoreta.viewModel.EventViewModel


@Composable
fun SettingsScreen(navController: NavHostController) {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        FilledTonalButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                restartApp(context)
            }
        ) {
            Text("Cerrar sesión")
        }

        FilledTonalButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                navController.navigate(AppScreens.ModifyUserScreen.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true // Avoid multiple copies of the same screen
                    restoreState = true // If the user was in the middle of something
                }

            }
        ) {
            Text("Modificar usuario")
        }

        FilledTonalButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                navController.navigate(AppScreens.NotificationsScreen.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true // Avoid multiple copies of the same screen
                    restoreState = true // If the user was in the middle of something
                }

            }
        ) {
            Text("Notificaciones")
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