package com.example.frontendgestoreta.ui.screens_gestor

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.frontendgestoreta.navigation.AppScreens
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.NavigationBar
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.frontendgestoreta.R
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.frontendgestoreta.data.models.EventDTO
import com.example.frontendgestoreta.ui.components.AppHeaderImage
import com.example.frontendgestoreta.ui.components.CreateEventScreen
import com.example.frontendgestoreta.ui.components.MainTopBar
import com.example.frontendgestoreta.ui.screens_user.FallaNewsScreen
import com.example.frontendgestoreta.ui.screens_user.MapScreen
import com.example.frontendgestoreta.ui.screens_user.NewsDetailScreen
import com.example.frontendgestoreta.ui.screens_user.NewsScreen
import com.example.frontendgestoreta.ui.screens_user.SettingsScreen
import com.example.frontendgestoreta.ui.theme.FrontendGestoretaTheme
import com.example.frontendgestoreta.viewModel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenGestor(
    authViewModel: AuthViewModel = viewModel()
) {
    val user = authViewModel.currentUserGestor.value
    val navController = rememberNavController()
    val navigationItems = listOf(
        AppScreens.NewsGestor,
        AppScreens.Members,
        AppScreens.FallaSettings

    )
    var showCreateEventScreen by remember { mutableStateOf(false) }
    var topBarTitle by remember { mutableStateOf(AppScreens.NewsGestor.title ?: "") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ){
        AppHeaderImage()
        Scaffold(
            topBar = {
                MainTopBar(
                title = topBarTitle,
                navController = navController
                )
            },
            bottomBar = {
                NavigationBar (
                    containerColor = MaterialTheme.colorScheme.tertiary
                ){
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination

                    navigationItems.forEach { screen ->
                        NavigationBarItem(
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = MaterialTheme.colorScheme.secondary,
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                            ),
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true // Avoid multiple copies of the same screen
                                    restoreState = true // If the user was in the middle of something
                                }
                            },
                            icon = {
                                Icon(
                                    painterResource(id = screen.icon!!),
                                    contentDescription = screen.title
                                )
                            }

                        )
                    }
                }
            },
            floatingActionButton = {
                // Botón redondo con + en el centro inferior
                FloatingActionButton(
                    onClick = {
                        showCreateEventScreen = true
                    },
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_falla_news), // Icono para añadir evento
                        contentDescription = "Crear evento",
                        modifier = Modifier.size(32.dp)
                    )
                }
            },
            floatingActionButtonPosition = FabPosition.Center
        ){innerPadding ->
            NavHost(
                navController = navController,
                startDestination = AppScreens.NewsGestor.route, //START DESTINATION
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(AppScreens.NewsGestor.route) {
                    topBarTitle = AppScreens.NewsGestor.title!!
                    NewsScreen()
                }
                composable(AppScreens.Members.route) {
                    topBarTitle = AppScreens.Members.title!!
                    MembersScreen()
                }
                composable(AppScreens.FallaSettings.route) {
                    topBarTitle = AppScreens.FallaSettings.title!!
                    FallaSettingsScreen()
                }
                composable(AppScreens.Settings.route) {
                    topBarTitle = AppScreens.Settings.title!!
                    SettingsScreen()
                }

                composable(
                    route = AppScreens.EventDetail.route,
                    arguments = listOf(navArgument("eventJson") { type = NavType.StringType })
                ) { backStackEntry ->
                    val eventJson = backStackEntry.arguments?.getString("eventJson")!!
                    val event = remember { com.google.gson.Gson().fromJson(eventJson, EventDTO::class.java) }

                    topBarTitle = event.titulo ?: "Detalle del Evento"

                    NewsDetailScreen(
                        event = event,
                        onInscribirseClick = {
                            println("Inscrito en: ${event.titulo}")
                        },
                        onBack = { navController.popBackStack() }
                    )
                }
            }

            if (showCreateEventScreen) {
                AlertDialog(
                    onDismissRequest = { showCreateEventScreen = false },
                    title = { Text("Crear Evento") },
                    text = {
                        CreateEventScreen(
                            onBack = { showCreateEventScreen = false }
                        )
                    },
                    confirmButton = {
                        TextButton(
                            onClick = { showCreateEventScreen = false }
                        ) {
                            Text("Cerrar")
                        }
                    }
                )

            }
        }
    }
}