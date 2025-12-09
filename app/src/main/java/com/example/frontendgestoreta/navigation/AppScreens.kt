package com.example.frontendgestoreta.navigation

import androidx.annotation.DrawableRes
import com.example.frontendgestoreta.R
import com.example.frontendgestoreta.data.models.EventDTO

sealed class AppScreens(
    val route: String,
    val title: String? = null,
    @DrawableRes val icon: Int? = null
) {
    object MainScreen : AppScreens(
        route = "main_screen",
        title = "Inicio"
    )
    object MainScreenGestor : AppScreens(
        route = "main_screen_gestor",
        title = "Inicio"
    )
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
        icon = R.drawable.ic_g_perfil
    )

    object Login : AppScreens(
        route = "login",
        title = "Inicio de sesión",
        icon = R.drawable.ic_arrow_back
    )

    object NotificationsScreen : AppScreens(
        route = "notifications_screen",
        title = "Notificaciones",
    )


    object ModifyUserScreen : AppScreens(
        route = "modify_user_screen",
        title = "Modificar Usuario",
    )

    object EventDetail : AppScreens(
        route = "event_detail/{eventJson}",
        title = "Detalle del Evento"
    ) {
        fun createRoute(event: EventDTO): String {
            val gson = com.google.gson.Gson()
            val eventJson = gson.toJson(event)
            return "event_detail/$eventJson"
        }
    }
}