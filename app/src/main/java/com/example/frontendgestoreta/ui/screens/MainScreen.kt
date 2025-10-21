package com.example.frontendgestoreta.ui.screens

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.frontendgestoreta.R
import androidx.compose.ui.tooling.preview.Preview
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

    //Recordar título actual de la pantalla
    var topBarTitle by remember { mutableStateOf(AppScreens.News.title ?: "") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        AppHeaderImage()
        Scaffold(
            containerColor = MaterialTheme.colorScheme.primary,
            topBar = {
                CenterAlignedTopAppBar(
                    windowInsets = WindowInsets(0.dp),
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        titleContentColor = MaterialTheme.colorScheme.tertiary,
                    ),
                    title = { Text(topBarTitle) })
            },
            bottomBar = {
                NavigationBar {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination

                    navigationItems.forEach { screen ->
                        NavigationBarItem(
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = MaterialTheme.colorScheme.secondary,
                                selectedIconColor = MaterialTheme.colorScheme.primary
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
            // Navigation graph
            NavHost(
                navController = navController,
                startDestination = AppScreens.News.route, //STAR DESTINATION
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
@Composable
fun AppHeaderImage() {
    Box(
        modifier = Modifier
            .padding(top = 40.dp)
            .padding(bottom = 10.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary), // Color de fondo para la barra
        contentAlignment = Alignment.Center
    ) { Image(
            painter = painterResource(id = R.drawable.ic_gestoreta),
            contentDescription = "Gestoreta", // Descripción para accesibilidad
            modifier = Modifier
                .height(50.dp), // Dale una altura fija a la barra del título
            contentScale = ContentScale.Fit // Asegura que la imagen se vea completa sin deformarse
        )
    }
}
@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    FrontendGestoretaTheme {
        MainScreen()
    }
}