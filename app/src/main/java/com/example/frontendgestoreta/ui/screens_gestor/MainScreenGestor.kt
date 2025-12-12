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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.NavigationBar
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
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
        AppScreens.Map,
        AppScreens.FallaSettings

    )
    var showCreateEventScreen by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var topBarTitle by remember { mutableStateOf(AppScreens.NewsGestor.title ?: "") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ){
        AppHeaderImage()
        Scaffold(
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            containerColor = colorResource(R.color.white),
            bottomBar = {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                Surface(
                    color = colorResource(R.color.black),
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                    shadowElevation = 10.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                ) {
                    NavigationBar(
                        containerColor = Color.Transparent,
                        modifier = Modifier.navigationBarsPadding()
                    ) {
                        val currentDestination = navBackStackEntry?.destination

                        navigationItems.forEach { screen ->
                            NavigationBarItem(
                                selected = currentDestination?.hierarchy?.any {
                                    it.route == screen.route
                                } == true,
                                onClick = {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    unselectedIconColor = colorResource(R.color.white),
                                    selectedIconColor = colorResource(R.color.purple_200),
                                    indicatorColor = Color.Transparent,
                                ),
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
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showCreateEventScreen = true },
                    shape = CircleShape,
                    containerColor = colorResource(R.color.black),
                    contentColor = colorResource(R.color.white),
                    modifier = Modifier
                        .size(68.dp)
                        .offset(y = 45.dp) // <--- más abajo
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add_new),
                        contentDescription = "Crear evento",
                        modifier = Modifier.fillMaxSize()
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
                ModalBottomSheet(
                    onDismissRequest = { showCreateEventScreen = false },
                    sheetState = sheetState,
                    containerColor = colorResource(R.color.white),
                    modifier = Modifier.fillMaxHeight(0.95f) // Evita altura infinita
                ) {
                    CreateEventScreen(
                        onBack = { showCreateEventScreen = false },
                        userGestor = user!!
                    )
                }
            }

        }
    }
}