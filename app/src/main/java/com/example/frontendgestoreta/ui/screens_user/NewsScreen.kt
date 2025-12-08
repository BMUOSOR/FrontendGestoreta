package com.example.frontendgestoreta.ui.screens_user

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.frontendgestoreta.data.models.EventFilterDTO
import com.example.frontendgestoreta.ui.components.EventFilterMenu
import com.example.frontendgestoreta.ui.components.NewsListItem
import com.example.frontendgestoreta.viewModel.AuthViewModel
import com.example.frontendgestoreta.viewModel.EventViewModel

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

    // Carga inicial
    LaunchedEffect(Unit) {
        viewModel.loadEvents()
    }

    // 2. Lógica de Filtrado
    val displayedEvents = remember(allEvents, selectedTab, currentUser, filter) {
        if (selectedTab == 1 && currentUser != null) {
            allEvents.filter { event -> event.idFalla == currentUser.idFalla }
        } else {
            allEvents
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {

            // Título
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Tablón de anuncios",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // --- BOTONES (TABS) ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp) // Espacio entre botones
            ) {
                SelectableButton(
                    text = "tu falla",
                    isSelected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                    // Nota: Ya NO pasamos modifier.weight(1f)
                )

                SelectableButton(
                    text = "fallas de tu sector",
                    isSelected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                )
            }

            // Menú de Filtros
            EventFilterMenu(
                filter = filter,
                onFilterChange = { filter = it },
                onApplyFilters = { viewModel.loadEventsWithFilter(it) },
                onClearFilter = {
                    filter = EventFilterDTO()
                    viewModel.loadEvents()
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Lista
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                if (isLoading) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(top = 50.dp),
                            contentAlignment = Alignment.Center
                        ) { CircularProgressIndicator(color = Color.Black) }
                    }
                } else {
                    if (displayedEvents.isEmpty()) {
                        item {
                            Text(
                                text = if (selectedTab == 1) "No hay eventos en tu falla." else "No hay noticias disponibles.",
                                modifier = Modifier.padding(20.dp), color = Color.Gray
                            )
                        }
                    } else {
                        items(displayedEvents) { event ->
                            NewsListItem(event = event, navController = navController)
                        }
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
        modifier = modifier.heightIn(min = 40.dp), // Altura estándar
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
        // Padding horizontal aumentado (16.dp) para que se vea bonito alrededor del texto
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