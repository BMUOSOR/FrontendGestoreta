package com.example.frontendgestoreta.ui.screens_user

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.frontendgestoreta.R
import com.example.frontendgestoreta.navigation.AppScreens
import com.example.frontendgestoreta.viewModel.AuthViewModel

@Composable
fun LoginScreen(navController: NavHostController, authViewModel: AuthViewModel) {
    Scaffold(
        contentWindowInsets = WindowInsets(0, 30, 0, 30)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // 1. Icono Grande
            Spacer(modifier = Modifier.height(30.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_gestoreta),
                contentScale = ContentScale.Fit,
                contentDescription = "Logo",
                modifier = Modifier
                    .size(160.dp)
                    .scale(1.8f)
                    .padding(bottom = 8.dp)
            )
            Spacer(modifier = Modifier.height(100.dp))

            // 2. Bienvenido
            Text(
                text = "Bienvenido",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic
                ),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // 3. Botón Usuario
            Button(
                onClick = {
                    authViewModel.loginAsUser {
                        navController.navigate(AppScreens.MainScreen.route) {
                            popUpTo("login") { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                // Usamos los colores por defecto del tema (generalmente Primary)
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Entrar como Usuario", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 4. Botón Gestor
            Button(
                onClick = {
                    authViewModel.loginAsGestor {
                        navController.navigate(AppScreens.MainScreenGestor.route) {
                            popUpTo("login") { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                // Forzamos el uso del color Naranja (Secondary)
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary // Asegura contraste (blanco o negro)
                )
            ) {
                Text("Entrar como Gestor", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Selecciona tu tipo de acceso para continuar",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}