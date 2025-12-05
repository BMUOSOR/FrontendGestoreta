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
import com.example.frontendgestoreta.ui.components.EditEventScreen
import com.example.frontendgestoreta.ui.components.MainTopBar
import com.example.frontendgestoreta.ui.screens_user.ModifyUserScreen
import com.example.frontendgestoreta.ui.screens_user.NotificationsScreen
import com.example.frontendgestoreta.ui.screens_user.SettingsScreen
import com.example.frontendgestoreta.ui.theme.FrontendGestoretaTheme
import com.example.frontendgestoreta.viewModel.AuthViewModel
import com.example.frontendgestoreta.viewModel.EventViewModel
import com.example.frontendgestoreta.ui.screens_user.NewsDetailScreen
import com.google.gson.Gson
import com.example.frontendgestoreta.ui.screens_user.NewsScreen

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
            floatingActionButtonPosition = FabPosition.Start
        ){innerPadding ->
            NavHost(
                navController = navController,
                startDestination = AppScreens.NewsGestor.route, //START DESTINATION
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(AppScreens.NewsGestor.route) {
                    topBarTitle = AppScreens.NewsGestor.title!!
                    NewsScreen(navController = navController)
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
                    SettingsGestorScreen(navController = navController)
                }
                composable(AppScreens.NotificationsScreen.route) {
                    topBarTitle = AppScreens.NotificationsScreen.title!!
                    NotificationsScreen()
                }

                composable(
                    route = AppScreens.EventDetail.route,
                    arguments = listOf(navArgument("eventJson") { type = NavType.StringType })
                ) { backStackEntry ->
                    val eventJson = backStackEntry.arguments?.getString("eventJson")!!
                    val event = remember { Gson().fromJson(eventJson, EventDTO::class.java) }

                    // 1. Instanciamos el ViewModel
                    val eventViewModel: EventViewModel = viewModel()
                    LaunchedEffect(Unit) { eventViewModel.loadEvents() }

                    // 2. Observamos Eventos y Fallas
                    val allEvents by eventViewModel.events.collectAsState(initial = emptyList())
                    val allFallas by eventViewModel.fallas.collectAsState(initial = emptyList()) // <--- NUEVO

                    // 3. Lógica para buscar el nombre del publicador
                    val nombrePublicador = remember(allFallas, event) {
                        allFallas.find { it.idFalla == event.idFalla }?.nombre ?: "Junta Central Fallera"
                    }

                    // 4. Lógica para eventos relacionados (igual que tenías)
                    val relatedEvents = remember(allEvents, event) {
                        allEvents
                            .filter { it.titulo != event.titulo }
                            .shuffled()
                            .take(3)
                    }

                    topBarTitle = "Detalle del Evento"

                    // 5. Pasamos el nombrePublicador a la pantalla
                    NewsDetailScreen(
                        event = event,
                        nombrePublicador = nombrePublicador, // <--- PASAMOS EL DATO AQUÍ
                        relatedEvents = relatedEvents,
                        onInscribirseClick = {
                            android.util.Log.d("Inscripción", "Usuario inscrito en: ${event.titulo}")
                        },
                        onBack = { navController.popBackStack() },
                        onRelatedEventClick = { relatedEvent ->
                            val json = Gson().toJson(relatedEvent)
                            // Asegúrate de que tu ruta base sea correcta aquí
                            // Si la ruta es "event_detail/{eventJson}", usa esto:
                            val route = AppScreens.EventDetail.route.replace("{eventJson}", json)
                            navController.navigate(route)
                        }
                    )
                    EditEventScreen(
                        event = event,
                        onEditClick = {
                            println("Editado: ${event.titulo}")
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
                            onBack = { showCreateEventScreen = false },
                            userGestor = user!!
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