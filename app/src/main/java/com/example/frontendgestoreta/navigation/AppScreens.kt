package com.example.frontendgestoreta.navigation

import androidx.annotation.DrawableRes
import com.example.frontendgestoreta.R

sealed class AppScreens(
    val route: String,
    val title: String? = null,
    @DrawableRes val icon: Int? = null
) {
    object FallaSettings : AppScreens(
        route = "falla_settings_screen",
        title = "Ajustes de falla",
        icon = R.drawable.ic_falla_news
    )
    object Members : AppScreens(
        route = "members_screen",
        title = "Miembros",
        icon = R.drawable.ic_fallas
    )
    object NewsGestor : AppScreens(
        route = "news_gestor_screen",
        title = "Noticias",
        icon = R.drawable.ic_newspaper
    )

    object News : AppScreens(
        route = "news_screen",
        title = "Noticias",
        icon = R.drawable.ic_newspaper
    )
    object Map : AppScreens(
        route = "map_screen",
        title = "Mapa",
        icon = R.drawable.ic_map
    )
    object Fallas : AppScreens(
        route = "fallas_screen",
        title = "Fallas",
        icon = R.drawable.ic_fallas
    )
    object FallaNews : AppScreens(
        route = "falla_news_screen",
        title = "Falla",
        icon = R.drawable.ic_falla_news
    )

    object Settings : AppScreens(
        route = "settings_screen",
        title = "Ajustes",
        icon = R.drawable.ic_settings
    )
}