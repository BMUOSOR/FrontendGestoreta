package com.example.frontendgestoreta.ui.screens_user

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontendgestoreta.viewModel.EventViewModel
import com.example.frontendgestoreta.ui.components.NewsListItem
import com.example.frontendgestoreta.data.models.EventDTO
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.example.frontendgestoreta.data.models.EventFilterDTO
import com.example.frontendgestoreta.ui.components.EventFilterMenu


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FallaNewsScreen(navController: NavController, viewModel: EventViewModel = viewModel()) {
    var filter by remember { mutableStateOf(EventFilterDTO()) }
    val events = viewModel.events.collectAsState(initial = emptyList())
    Log.d("Fallas News Screen", "Loading Events...")
    LaunchedEffect(Unit) { viewModel.loadEvents() }

    Scaffold (contentWindowInsets = WindowInsets(0,30,0,30)){ innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                EventFilterMenu(
                    filter = filter,
                    onFilterChange = { filter = it },
                    onApplyFilters = { finalFilter ->
                        viewModel.loadEventsWithFilter(finalFilter)
                    },
                    onClearFilter = {filter = EventFilterDTO()}
                )
            }
            
            items(events.value) { event ->
                NewsListItem(event = event, navController = navController)
            }
        }
    }
}
