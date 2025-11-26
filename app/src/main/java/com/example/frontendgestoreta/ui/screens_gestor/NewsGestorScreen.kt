package com.example.frontendgestoreta.ui.screens_gestor

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.frontendgestoreta.data.models.EventFilterDTO
import com.example.frontendgestoreta.ui.components.EventFilterMenu
import com.example.frontendgestoreta.ui.components.NewsListItem
import com.example.frontendgestoreta.viewModel.EventViewModel

@Composable
fun NewsGestorScreen(navController: NavController, viewModel: EventViewModel = viewModel()) {
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

            items(events.value) { event ->
                NewsListItem(event = event, navController = navController)
            }
        }
    }
}