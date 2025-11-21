package com.example.frontendgestoreta.ui.components

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.frontendgestoreta.navigation.AppScreens
import com.example.frontendgestoreta.ui.theme.FrontendGestoretaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
    title: String,
    navController: NavController,
) {
    CenterAlignedTopAppBar(
        windowInsets = WindowInsets(0),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            titleContentColor = MaterialTheme.colorScheme.primary
        ),
        title = { Text(title) },
        actions = {
            IconButton(
                onClick = {
                    navController.navigate(AppScreens.Settings.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            ) {
                Icon(
                    painter = painterResource(id = AppScreens.Settings.icon!!),
                    contentDescription = AppScreens.Settings.title
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun MainTopBarPreview() {
    FrontendGestoretaTheme {
        val navController = rememberNavController()
        MainTopBar(
            title = "Título de Prueba",
            navController = navController
        )
    }
}
