package com.example.frontendgestoreta.ui.screens_gestor

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.NavigationBar
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.frontendgestoreta.R
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.frontendgestoreta.data.models.MemberDTO
import com.example.frontendgestoreta.ui.components.AppHeaderImage
import com.example.frontendgestoreta.ui.components.MainTopBar
import com.example.frontendgestoreta.ui.screens_user.FallaNewsScreen
import com.example.frontendgestoreta.ui.screens_user.MapScreen
import com.example.frontendgestoreta.ui.screens_user.NewsScreen
import com.example.frontendgestoreta.ui.screens_user.SettingsScreen
import com.example.frontendgestoreta.ui.theme.FrontendGestoretaTheme
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults

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

    // Estado para controlar la visibilidad de CreateEventScreen
    var showCreateEventScreen by remember { mutableStateOf(false) }

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
            floatingActionButtonPosition = FabPosition.Center
        ){ innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                NavHost(
                    navController = navController,
                    startDestination = AppScreens.NewsGestor.route, //START DESTINATION
                    modifier = Modifier.fillMaxSize()
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
                        FallaSettingsScreen()
                    }
                    composable(AppScreens.Settings.route) {
                        topBarTitle = AppScreens.Settings.title!!
                        SettingsScreen()
                    }
                }

                // Mostrar CreateEventScreen como overlay/dialog cuando sea necesario
                if (showCreateEventScreen) {
                    AlertDialog(
                        onDismissRequest = { showCreateEventScreen = false },
                        title = { Text("Crear Evento") },
                        text = {
                            CreateEventScreen(
                                onBack = { showCreateEventScreen = false }
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
}

@Composable
fun CreateEventScreen(onBack: () -> Unit) {
    // Estados para los campos del formulario
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var ubicacion by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Crear Nuevo Evento",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Campo Título
        Text(
            text = "Título",
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Ingresa el título del evento") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo Descripción
        Text(
            text = "Descripción",
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(4.dp))

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            placeholder = { Text("Describe el evento") },
            singleLine = false,
            maxLines = 4
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Ubicación",
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        
        OutlinedTextField(
            value = ubicacion,
            onValueChange = { ubicacion = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("¿Dónde será el evento?") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        Spacer(modifier = Modifier.height(24.dp))

        // Botones de acción
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = { onBack() }
            ) {
                Text("Cancelar")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    // Lógica para crear evento con los datos
                    val nuevoEvento = mapOf(
                        "titulo" to titulo,
                        "descripcion" to descripcion,
                        "ubicacion" to ubicacion
                    )
                    println("Evento creado: $nuevoEvento")

                    onBack()
                },
                enabled = titulo.isNotBlank() && descripcion.isNotBlank() && ubicacion.isNotBlank()
            ) {
                Text("Crear Evento")
            }
        }
    }
}