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
import androidx.compose.material3.NavigationBar
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.frontendgestoreta.R
import androidx.compose.ui.tooling.preview.Preview
import com.example.frontendgestoreta.ui.components.AppHeaderImage
import com.example.frontendgestoreta.ui.components.MainTopBar
import com.example.frontendgestoreta.ui.screens_user.FallaNewsScreen
import com.example.frontendgestoreta.ui.screens_user.FallasScreen
import com.example.frontendgestoreta.ui.screens_user.MapScreen
import com.example.frontendgestoreta.ui.screens_user.NewsScreen
import com.example.frontendgestoreta.ui.screens_user.SettingsScreen
import com.example.frontendgestoreta.ui.theme.FrontendGestoretaTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenGestor() {
    val navController = rememberNavController()
    val navigationItems = listOf(
        AppScreens.NewsGestor,
        AppScreens.Members,
        AppScreens.FallaSettings

    )
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
            }
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
                    FallasScreen()
                }
                composable(AppScreens.Settings.route) {
                    topBarTitle = AppScreens.Settings.title!!
                    SettingsScreen()
                }
            }
        }
    }
}