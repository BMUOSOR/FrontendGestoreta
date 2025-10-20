package com.example.frontendgestoreta.navigation

import com.example.frontendgestoreta.ui.screens.MapScreen

sealed class AppScreens (val route: String){
    object News: AppScreens("news_screen")
    object Map: AppScreens("map_screen")
    object Fallas : AppScreens("fallas_screen")
    object FallaNews : AppScreens("falla_news_screen")
    object Settings : AppScreens("settings_screen")
}