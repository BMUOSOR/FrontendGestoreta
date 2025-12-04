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

    // Estados de datos
    private val _events = MutableStateFlow<List<EventDTO>>(emptyList())
    private val _fallas = MutableStateFlow<List<FallaDTO>>(emptyList())
    private val _tags = MutableStateFlow<List<TagDTO>>(emptyList())

    val events: MutableStateFlow<List<EventDTO>> = _events
    val fallas: StateFlow<List<FallaDTO>> = _fallas
    val tags: StateFlow<List<TagDTO>> = _tags

    // Estado de carga (Inicializado en true para que al abrir la app salga cargando)
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadEvents() {
        viewModelScope.launch {
            // Aseguramos que empiece cargando
            _isLoading.value = true

            try {
                Log.d("EventViewModel", "Iniciando carga de datos...")

                // Realizamos las llamadas a la API
                val eventsResult = repository.getAllEvents()
                val fallasResult = fallaRepository.getAllFallas()
                val tagsResult = tagRepository.getAllTags()

                // Actualizamos los estados
                _events.value = eventsResult
                _fallas.value = fallasResult
                _tags.value = tagsResult

                Log.d("EventViewModel", "Datos cargados correctamente: ${eventsResult.size} eventos.")

            } catch (e: Exception) {
                // Manejo de errores
                println("Error al cargar datos: ${e.message}")
                Log.e("EventViewModel", "Error al cargar datos: ${e.message}")
                e.printStackTrace()
            } finally {
                // ESTO ES IMPORTANTE:
                // El bloque finally se ejecuta siempre, haya error o éxito.
                // Aquí quitamos el spinner de carga.
                _isLoading.value = false
            }
        }
    }

    fun loadEventsWithFilter(filter: EventFilterDTO) {
        viewModelScope.launch {
            try {
                Log.d("EventViewModel", "Antes de eventFilter: " + filter.beforeDate.toString())
                Log.d("EventViewModel", "Despues de eventFilter: " + filter.afterDate.toString())

                // Opcional: Podrías poner isLoading = true aquí también si quieres
                // que salga el spinner al filtrar, aunque a veces es mejor que sea transparente.
                val eventsResult = repository.filterEvents(filter)
                _events.value = eventsResult

            } catch (e: Exception) {
                Log.e("EventViewModel", "Error filtrando eventos: ${e.message}")
            }
        }
    }

    fun createEvent(event: EventDTO) {
        viewModelScope.launch {
            try {
                repository.postEvent(event)
                // Opcional: Recargar eventos tras crear uno nuevo
                // loadEvents()
            } catch (e: Exception) {
                Log.e("EventViewModel", "Error creando el evento: ${e.message}")
            }
        }
    }

    fun updateEvent(event: EventDTO) {
        viewModelScope.launch {
            try {
                repository.updateEvent(event)
            } catch(e: Exception) {
                Log.e("EventViewModel", "Error al editar el evento: ${e.message}")
            }
        }
    }
}