package com.example.frontendgestoreta.ui.screens_user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.frontendgestoreta.data.models.EventDTO
import com.example.frontendgestoreta.navigation.AppScreens
import com.example.frontendgestoreta.ui.components.AppHeaderImage
import com.example.frontendgestoreta.ui.components.MainTopBar
// Asegúrate de importar tus pantallas correctamente
import com.example.frontendgestoreta.ui.screens_user.FallasScreen
import com.example.frontendgestoreta.ui.theme.FrontendGestoretaTheme
import com.example.frontendgestoreta.viewModel.AuthViewModel
import com.example.frontendgestoreta.viewModel.EventViewModel
import com.google.gson.Gson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    authViewModel: AuthViewModel,
    eventViewModel: EventViewModel = viewModel()
) {

    val user = authViewModel.currentUser.value
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Carga de datos
    LaunchedEffect(Unit) {
        eventViewModel.loadEvents()
    }

    val isLoading by eventViewModel.isLoading.collectAsState()
    val allEvents by eventViewModel.events.collectAsState()
    val allFallas by eventViewModel.fallas.collectAsState()

    val navigationItems = listOf(
        AppScreens.News,
        AppScreens.Map,
        AppScreens.Fallas,
        AppScreens.FallaNews
    )

    // Control del título de la TopBar
    var topBarTitle by remember { mutableStateOf(AppScreens.News.title ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        AppHeaderImage()
        Scaffold(
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            containerColor = Color.Transparent,
            topBar = {
                // Ocultamos TopBar si está cargando, o si estamos en mapa/detalle
                if (!isLoading && currentRoute != AppScreens.Map.route && !currentRoute.toString().contains("event_detail")) {
                    MainTopBar(
                        title = topBarTitle,
                        navController = navController
                    )
                }
            },
            bottomBar = {
                if (!isLoading) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.tertiary
                    ) {
                        val currentDestination = navBackStackEntry?.destination

                        navigationItems.forEach { screen ->
                            NavigationBarItem(
                                colors = NavigationBarItemDefaults.colors(
                                    indicatorColor = MaterialTheme.colorScheme.secondary,
                                    selectedIconColor = MaterialTheme.colorScheme.onSecondary,
                                ),
                                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                onClick = {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
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
                }
            }
        ) { innerPadding ->

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(48.dp)
                    )
                }
            } else {
                NavHost(
                    navController = navController,
                    startDestination = AppScreens.News.route,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable(AppScreens.News.route) {
                        topBarTitle = AppScreens.News.title!!
                        NewsScreen(navController = navController)
                    }
                    composable(AppScreens.Map.route) {
                        topBarTitle = AppScreens.Map.title!!
                        MapScreen()
                    }
                    composable(AppScreens.Fallas.route) {
                        topBarTitle = AppScreens.Fallas.title!!
                        FallasScreen()
                    }
                    composable(AppScreens.FallaNews.route) {
                        topBarTitle = AppScreens.FallaNews.title!!
                        FallaNewsScreen(navController = navController)
                    }

                    // Detalle del evento
                    composable(
                        route = AppScreens.EventDetail.route,
                        arguments = listOf(navArgument("eventJson") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val eventJson = backStackEntry.arguments?.getString("eventJson")!!
                        val event = remember { Gson().fromJson(eventJson, EventDTO::class.java) }

                        val nombrePublicador = remember(allFallas, event) {
                            allFallas.find { it.idFalla == event.idFalla }?.nombre
                                ?: "Junta Central Fallera"
                        }

                        val relatedEvents = remember(allEvents, event) {
                            allEvents
                                .filter { it.titulo != event.titulo }
                                .shuffled()
                                .take(3)
                        }

                        // Actualizamos título manualmente para detalles
                        LaunchedEffect(Unit) {
                            topBarTitle = "Detalle del Evento"
                        }

                        NewsDetailScreen(
                            event = event,
                            nombrePublicador = nombrePublicador,
                            relatedEvents = relatedEvents,
                            onInscribirseClick = {
                                android.util.Log.d(
                                    "Inscripción",
                                    "Usuario inscrito en: ${event.titulo}"
                                )
                            },
                            onBack = { navController.popBackStack() },
                            onRelatedEventClick = { relatedEvent ->
                                val json = Gson().toJson(relatedEvent)
                                val route = AppScreens.EventDetail.route.replace("{eventJson}", json)
                                navController.navigate(route)
                            }
                        )
                    }

                    // Otras pantallas dentro del NavHost
                    composable(AppScreens.Settings.route) {
                        topBarTitle = AppScreens.Settings.title!!
                        SettingsScreen(navController = navController)
                    }

                    composable(AppScreens.ModifyUserScreen.route) {
                        topBarTitle = AppScreens.ModifyUserScreen.title!!
                        ModifyUserScreen(                        onBack = { navController.popBackStack() },
                            member =  user!!,
                            viewModel = viewModel(),
                        )
                    }

                    composable(AppScreens.NotificationsScreen.route) {
                        topBarTitle = AppScreens.NotificationsScreen.title!!
                        NotificationsScreen()
                    }
                } // Fin del NavHost
            }
        }
    }
}