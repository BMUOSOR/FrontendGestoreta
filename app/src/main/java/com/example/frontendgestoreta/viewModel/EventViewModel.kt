package com.example.frontendgestoreta.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontendgestoreta.data.api.RetrofitClient
import com.example.frontendgestoreta.data.models.EventDTO
import com.example.frontendgestoreta.data.models.EventFilterDTO
import com.example.frontendgestoreta.data.models.FallaDTO
import com.example.frontendgestoreta.data.models.TagDTO
import com.example.frontendgestoreta.repository.EventRepository
import com.example.frontendgestoreta.repository.FallaRepository
import com.example.frontendgestoreta.repository.TagRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EventViewModel : ViewModel() {
    private val repository = EventRepository(RetrofitClient.apiService)

    private val fallaRepository = FallaRepository(RetrofitClient.apiService)

    private val tagRepository = TagRepository(RetrofitClient.apiService)

    private val _events = MutableStateFlow<List<EventDTO>>(emptyList())
    private val _fallas = MutableStateFlow<List<FallaDTO>>(emptyList())
    private val _tags = MutableStateFlow<List<TagDTO>>(emptyList())
    val events: MutableStateFlow<List<EventDTO>> = _events // events solo lectura

    val fallas: StateFlow<List<FallaDTO>> = _fallas

    val tags: StateFlow<List<TagDTO>> =_tags


    fun loadEvents(){
        viewModelScope.launch {
            try {
                Log.d("EventViewModel", "Iniciando carga de datos...")
                val eventsResult = repository.getAllEvents()
                val fallasResult = fallaRepository.getAllFallas()
                val tagsResult = tagRepository.getAllTags()
                _events.value = eventsResult
                _fallas.value = fallasResult
                _tags.value = tagsResult
                Log.d("EventViewModel", "Datos cargados: ${eventsResult.size} miembros, ${eventsResult.size} solicitudes.")

            }catch (e: Exception) {
                println("Error al cargar datos: ${e.message}")
                Log.e("EventViewModel","Error al cargar datos: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun loadEventsWithFilter(filter: EventFilterDTO) {
        viewModelScope.launch {
            try {
                Log.d("DEBUG","Id de la tag: " + filter.tagId.toString())


                val eventsResult = repository.filterEvents(filter)
                _events.value = eventsResult


            } catch (e: Exception) {
                Log.e("EventViewModel", "Error filtrando eventos: ${e.message}")
            }
        }
    }

}