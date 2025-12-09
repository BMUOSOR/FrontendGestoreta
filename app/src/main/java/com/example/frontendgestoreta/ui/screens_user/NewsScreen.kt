package com.example.frontendgestoreta.ui.screens_user

import android.util.Base64
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan // IMPORTANTE
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.frontendgestoreta.data.models.EventDTO
import com.example.frontendgestoreta.data.models.EventFilterDTO
import com.example.frontendgestoreta.data.models.Tag
import com.example.frontendgestoreta.ui.components.AyuntamientoNewsItem
import com.example.frontendgestoreta.ui.components.EventFilterMenu
import com.example.frontendgestoreta.ui.components.NewsListItem
import com.example.frontendgestoreta.viewModel.AuthViewModel
import com.example.frontendgestoreta.viewModel.EventViewModel
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.OffsetDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(
    navController: NavController,
    viewModel: EventViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    // 1. Configuración de estados
    var filter by remember { mutableStateOf(EventFilterDTO()) }
    var selectedTab by remember { mutableStateOf(0) }

    // Recogemos estados
    val allEventsState = viewModel.events.collectAsState(initial = emptyList())
    val isLoadingState = viewModel.isLoading.collectAsState()
    val currentUserState = authViewModel.currentUser

    val allEvents = allEventsState.value
    val isLoading = isLoadingState.value
    val currentUser = currentUserState.value

    val ayuntamientoEvents = listOf(
        EventDTO(
            idEvento = 9991,
            createdAt = OffsetDateTime.now(),
            fecha = LocalDate.now().plusDays(2),
            hora = "20:30",
            ubicacion = """{"direccion":"Plaza del Ayuntamiento, Valencia","ciudad":"Valencia"}""",
            idFalla = 0,
            idAnuncio = 1001,
            idEtiqueta = 1,
            publico = true,
            titulo = "València celebrará un espectáculo de fuegos artificiales navideño",
            descripcion = "El Ayuntamiento de València ha organizado un gran espectáculo pirotécnico que iluminará el cielo nocturno con miles de luces y colores. Se esperan músicos en vivo, puestos de comida y actividades familiares, haciendo de esta una experiencia inolvidable para todos los asistentes.",
            maxPersonas = 5000,
            tag = Tag.Pirotecnia,
            imagen = null
        ),
        EventDTO(
            idEvento = 9992,
            createdAt = OffsetDateTime.now(),
            fecha = LocalDate.now().plusDays(5),
            hora = "10:00",
            ubicacion = """{"direccion":"Museo de Bellas Artes, Valencia","ciudad":"Valencia"}""",
            idFalla = 0,
            idAnuncio = 1002,
            idEtiqueta = 2,
            publico = true,
            titulo = "Programación especial de actividades culturales en museos",
            descripcion = "Durante todo este mes, los museos municipales abrirán sus puertas para ofrecer talleres, visitas guiadas y exposiciones interactivas. Desde pintura y escultura hasta música y teatro, los visitantes podrán disfrutar de una experiencia educativa y entretenida pensada para todas las edades.",
            maxPersonas = 300,
            tag = Tag.Cultura,
            imagen = null
        ),
        EventDTO(
            idEvento = 9993,
            createdAt = OffsetDateTime.now(),
            fecha = LocalDate.now().plusDays(1),
            hora = "09:00",
            ubicacion = """{"direccion":"Calles del centro histórico, Valencia","ciudad":"Valencia"}""",
            idFalla = 0,
            idAnuncio = 1003,
            idEtiqueta = 3,
            publico = true,
            titulo = "Cortes de tráfico por pasacalles este domingo",
            descripcion = "Debido a la celebración de varios pasacalles tradicionales, algunas calles del centro de Valencia estarán cerradas al tráfico durante la mañana y el mediodía. Se recomienda a los conductores planificar rutas alternativas y a los peatones disfrutar de los desfiles con seguridad.",
            maxPersonas = 0,
            tag = Tag.Pasacalle,
            imagen = null
        ),
        EventDTO(
            idEvento = 9994,
            createdAt = OffsetDateTime.now(),
            fecha = LocalDate.now().plusWeeks(1),
            hora = "18:00",
            ubicacion = """{"direccion":"Plaza del Ayuntamiento, Valencia","ciudad":"Valencia"}""",
            idFalla = 0,
            idAnuncio = 1004,
            idEtiqueta = 4,
            publico = true,
            titulo = "Gran Verbena en la Plaza del Ayuntamiento",
            descripcion = "Prepárate para disfrutar de la gran verbena popular que tendrá lugar este sábado en la Plaza del Ayuntamiento. Habrá música en vivo, bailes tradicionales, puestos de comida y actividades para niños. Un evento perfecto para toda la familia y para celebrar la cultura valenciana.",
            maxPersonas = 2000,
            tag = Tag.Verbena,
            imagen = null
        )
    )

    // Carga inicial
    LaunchedEffect(Unit) {
        viewModel.loadEvents()
    }

    // 2. Lógica de Filtrado
    val displayedEvents = remember(allEvents, selectedTab, currentUser, filter) {
        if (selectedTab == 1 && currentUser != null) {
            allEvents.filter { event ->
                event.idFalla == currentUser.idFalla
            }
        } else {
            allEvents.filter { event ->
                event.publico == true
            }
        }
    }

    // Scroll Automático Carrusel
    val scrollState = rememberLazyListState()
    LaunchedEffect(Unit) {
        while (isActive) {
            delay(20)
            try {
                scrollState.scrollBy(2f)
                if (!scrollState.canScrollForward) {
                    scrollState.scrollToItem(0)
                }
            } catch (e: Exception) {}
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        containerColor = Color.White
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp), // Padding global
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {

            // --- SECCIÓN 1: CABECERA (Ocupa las 2 columnas) ---
            item(span = { GridItemSpan(2) }) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Tablón de anuncios",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 8.dp) // menos padding abajo
                    )

                    // Carrusel con altura reducida (antes 400.dp)
                    LazyRow(
                        state = scrollState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp), // <- altura mucho menor
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(vertical = 6.dp) // pequeño padding vertical
                    ) {
                        items(ayuntamientoEvents) { event ->
                            AyuntamientoNewsItem(event = event, onClick = {
                                val json = Gson().toJson(event)
                                val encodedJson = Base64.encodeToString(json.toByteArray(), Base64.URL_SAFE or Base64.NO_WRAP)
                                navController.navigate("event_detail/$encodedJson") {
                                    launchSingleTop = true
                                }
                            })
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp)) // espacio reducido entre carrusel y botones


                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SelectableButton(
                            text = "comunidad",
                            isSelected = selectedTab == 0,
                            onClick = { selectedTab = 0 }
                        )

                        SelectableButton(
                            text = "tu falla",
                            isSelected = selectedTab == 1,
                            onClick = { selectedTab = 1 }
                        )
                    }

                    // Filtros con un margen superior más pequeño
                    EventFilterMenu(
                        filter = filter,
                        onFilterChange = { filter = it },
                        onApplyFilters = { viewModel.loadEventsWithFilter(it) },
                        onClearFilter = {
                            filter = EventFilterDTO()
                            viewModel.loadEvents()
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp)) // pequeño espacio antes del grid
                }
            }


            // --- SECCIÓN 2: LISTA DE NOTICIAS (GRID REAL) ---
            if (isLoading) {
                item(span = { GridItemSpan(2) }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 50.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.Black)
                    }
                }
            } else {
                if (displayedEvents.isEmpty()) {
                    item(span = { GridItemSpan(2) }) {
                        Text(
                            text = if (selectedTab == 1) "No hay eventos en tu falla." else "No hay noticias disponibles.",
                            modifier = Modifier.padding(20.dp),
                            color = Color.Gray
                        )
                    }
                } else {
                    // Aquí se pintan las tarjetas en 2 columnas
                    items(displayedEvents) { event ->
                        NewsListItem(event = event, navController = navController)
                    }
                }
            }
        }
    }
}

// --- BOTÓN AJUSTADO AL TEXTO ---
@Composable
fun SelectableButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderStroke = if (isSelected) BorderStroke(1.dp, Color.Black) else null
    val elevation = if (isSelected) 0.dp else 3.dp

    Button(
        onClick = onClick,
        modifier = modifier.heightIn(min = 40.dp),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black,
        ),
        border = borderStroke,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = elevation,
            pressedElevation = 0.dp,
            hoveredElevation = elevation,
            focusedElevation = 0.dp
        ),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 0.dp)
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            maxLines = 1
        )
    }
}