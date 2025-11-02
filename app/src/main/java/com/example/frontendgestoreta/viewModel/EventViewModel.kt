package com.example.frontendgestoreta.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontendgestoreta.data.api.RetrofitClient
import com.example.frontendgestoreta.data.models.EventDTO
import com.example.frontendgestoreta.repository.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class EventViewModel : ViewModel() {
    private val repository = EventRepository(RetrofitClient.apiService)

    private val _events = MutableStateFlow<List<EventDTO>>(emptyList())
    val events: MutableStateFlow<List<EventDTO>> = _events // events solo lectura

    fun loadEvents(){
        viewModelScope.launch {
            try {
                Log.d("MembersViewModel", "Iniciando carga de datos...")
                val eventsResult = repository.getAllEvents()
                _events.value = eventsResult
                Log.d("MembersViewModel", "Datos cargados: ${eventsResult.size} miembros, ${eventsResult.size} solicitudes.")

            }catch (e: Exception) {
                println("Error al cargar datos: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}