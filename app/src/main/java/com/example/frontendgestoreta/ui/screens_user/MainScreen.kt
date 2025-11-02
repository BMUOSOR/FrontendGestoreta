package com.example.frontendgestoreta.ui.screens_user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.frontendgestoreta.navigation.AppScreens
import com.example.frontendgestoreta.ui.components.AppHeaderImage
import com.example.frontendgestoreta.ui.components.MainTopBar
import com.example.frontendgestoreta.ui.theme.FrontendGestoretaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
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
                    FallaNewsScreen()
                }
                composable(AppScreens.Settings.route) {
                    topBarTitle = AppScreens.Settings.title!!
                    SettingsScreen()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    FrontendGestoretaTheme {
        MainScreen()
    }
}