package com.example.frontendgestoreta.ui.screens_gestor

import android.util.Base64
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.frontendgestoreta.R
import com.example.frontendgestoreta.data.models.EventDTO
import com.example.frontendgestoreta.data.models.EventFilterDTO
import com.example.frontendgestoreta.data.models.Tag
import com.example.frontendgestoreta.ui.components.AyuntamientoNewsItem
import com.example.frontendgestoreta.ui.components.CreateEventScreen
import com.example.frontendgestoreta.ui.components.EditEventScreen
import com.example.frontendgestoreta.ui.components.EventFilterMenu
import com.example.frontendgestoreta.ui.screens_gestor.NewsGestorListItem
import com.example.frontendgestoreta.viewModel.AuthViewModel
import com.example.frontendgestoreta.viewModel.EventViewModel
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.time.LocalDate
import java.time.OffsetDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsGestorScreen(
    navController: NavController,
    viewModel: EventViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    // 1. Configuración de estados
    var filter by remember { mutableStateOf(EventFilterDTO()) }
    var selectedTab by remember { mutableStateOf(1) }

    var selectedEvent by remember { mutableStateOf<EventDTO?>(null) }

    // Recogemos estados
    val allEventsState = viewModel.events.collectAsState(initial = emptyList())
    val isLoadingState = viewModel.isLoading.collectAsState()
    val currentUserState = authViewModel.currentUserGestor

    val allEvents = allEventsState.value
    val isLoading = isLoadingState.value
    val currentUser by authViewModel.currentUserGestor.collectAsState(initial = null)

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showEditEventScreen by remember { mutableStateOf(false) }

    // Carga inicial
    LaunchedEffect(Unit) {
        viewModel.loadEvents()
    }

    // 2. Lógica de Filtrado
    val displayedEvents = remember(allEvents, selectedTab, currentUser, filter) {
        if (currentUser == null) {
            emptyList()
        } else {
            allEvents.filter { event ->
                event.idFalla == currentUser?.falla
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
                        text = "Editar eventos",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 8.dp) // menos padding abajo
                    )

                    Spacer(modifier = Modifier.height(16.dp)) // espacio reducido entre carrusel y botones

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
                        NewsGestorListItem(
                            event = event,
                            onClick = {
                                selectedEvent = event
                                showEditEventScreen = true
                            })
                    }

                }
            }
        }
    }
    if (showEditEventScreen && selectedEvent != null) {
        ModalBottomSheet(
            onDismissRequest = { showEditEventScreen = false },
            sheetState = sheetState,
            containerColor = colorResource(R.color.white),
            modifier = Modifier.fillMaxHeight(0.95f) // Evita altura infinita
        ) {
            EditEventScreen(
                onBack = { showEditEventScreen = false },
                userGestor = currentUser!!,
                event = selectedEvent!!
            )
        }
    }
}
