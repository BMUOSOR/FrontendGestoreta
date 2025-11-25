package com.example.frontendgestoreta.ui.screens_user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
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
import com.example.frontendgestoreta.ui.screens_user.FallasScreen
import com.example.frontendgestoreta.ui.theme.FrontendGestoretaTheme
import com.example.frontendgestoreta.viewModel.AuthViewModel
import com.google.gson.Gson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    authViewModel: AuthViewModel
) {

    val user = authViewModel.currentUser.value
    val navController = rememberNavController()
    val navigationItems = listOf(
        AppScreens.News,
        AppScreens.Map,
        AppScreens.Fallas,
        AppScreens.FallaNews
    )
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
                MainTopBar(
                    title = topBarTitle,
                    navController = navController
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.tertiary
                ) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
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
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = AppScreens.News.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(AppScreens.News.route) {
                    topBarTitle = AppScreens.News.title!!
                    NewsScreen()
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
                composable(
                    route = AppScreens.EventDetail.route,
                    arguments = listOf(navArgument("eventJson") { type = NavType.StringType })
                ) { backStackEntry ->
                    val eventJson = backStackEntry.arguments?.getString("eventJson")!!
                    val event = remember { Gson().fromJson(eventJson, EventDTO::class.java) }

                    topBarTitle = "Detalle del Evento"

                    NewsDetailScreen(
                        event = event,
                        onInscribirseClick = {
                            // Aquí pondrás la inscripción real más tarde
                            // Por ahora, solo un log
                            android.util.Log.d("Inscripción", "Usuario inscrito en: ${event.titulo}")
                        },
                        onBack = { navController.popBackStack() }
                    )
                }
                composable(AppScreens.Settings.route) {
                    topBarTitle = AppScreens.Settings.title!!
                    SettingsScreen(navController = navController)
                }
                composable(AppScreens.SubscriptionsScreen.route) {
                    topBarTitle = AppScreens.SubscriptionsScreen.title!!
                    SubscriptionsScreen()
                }
                composable(AppScreens.EliminateSubscriptionsScreen.route) {
                    topBarTitle = AppScreens.EliminateSubscriptionsScreen.title!!
                    EliminateSubscriptionsScreen()
                }
                composable(AppScreens.ModifyUserScreen.route) {
                    topBarTitle = AppScreens.ModifyUserScreen.title!!
                    ModifyUserScreen()
                }
                composable(AppScreens.NotificationsScreen.route) {
                    topBarTitle = AppScreens.NotificationsScreen.title!!
                    NotificationsScreen()
                }
            }
        }
    }
}
