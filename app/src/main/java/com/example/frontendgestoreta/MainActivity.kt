package com.example.frontendgestoreta

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.frontendgestoreta.navigation.AppScreens
import com.example.frontendgestoreta.ui.screens_user.LoginScreen
import com.example.frontendgestoreta.ui.screens_user.MainScreen
import com.example.frontendgestoreta.ui.screens_gestor.MainScreenGestor
import com.example.frontendgestoreta.ui.screens_user.FallasScreen
import com.example.frontendgestoreta.ui.theme.FrontendGestoretaTheme
import com.example.frontendgestoreta.viewModel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            FrontendGestoretaTheme {
                val navController = rememberNavController()
                val authViewModel: AuthViewModel = viewModel()

                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = "login"
                    ) {
                        composable("login") {
                            LoginScreen(
                                navController = navController,
                                authViewModel = authViewModel
                            )
                        }

                        composable(AppScreens.MainScreen.route) {
                            MainScreen(authViewModel = authViewModel)
                        }

                        composable(AppScreens.MainScreenGestor.route) {
                            MainScreenGestor(authViewModel = authViewModel)
                        }

                        composable(AppScreens.Fallas.route) {
                            FallasScreen(authViewModel = authViewModel)
                        }
                    }
                }
            }
        }
    }
}

