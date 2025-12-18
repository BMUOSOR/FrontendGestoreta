package com.example.frontendgestoreta.viewModel

import android.content.Context
import android.net.Uri
import android.util.Log
import android.util.Log.println
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontendgestoreta.data.api.RetrofitClient
import com.example.frontendgestoreta.data.models.EventDTO
import com.example.frontendgestoreta.data.models.EventFilterDTO
import com.example.frontendgestoreta.data.models.FallaDTO
import com.example.frontendgestoreta.data.models.Tag
import com.example.frontendgestoreta.repository.EventRepository
import com.example.frontendgestoreta.repository.FallaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.http.Part
import retrofit2.http.Path
import java.sql.DriverManager.println
import java.util.Collections.emptyList

class EventViewModel : ViewModel() {

    private val repository = EventRepository(RetrofitClient.apiService)
    private val fallaRepository = FallaRepository(RetrofitClient.apiService)

    private val _events = MutableStateFlow<List<EventDTO>>(emptyList())
    private val _fallas = MutableStateFlow<List<FallaDTO>>(emptyList())
    private val _tags = MutableStateFlow<List<Tag>>(emptyList())
    val events: MutableStateFlow<List<EventDTO>> = _events
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    val fallas: StateFlow<List<FallaDTO>> = _fallas

    val tags: StateFlow<List<Tag>> =_tags


    fun loadEvents(){
        viewModelScope.launch {
            _isLoading.value = true
            try {
                Log.d("EventViewModel", "Iniciando carga de datos...")
                val eventsResult = repository.getAllEvents()
                val fallasResult = fallaRepository.getAllFallas()
                _events.value = eventsResult
                _fallas.value = fallasResult
                Log.d("EventViewModel", "Datos cargados: ${eventsResult.size} miembros, ${eventsResult.size} solicitudes.")

            }catch (e: Exception) {
                println("Error al cargar datos: ${e.message}")
                Log.e("EventViewModel","Error al cargar datos: ${e.message}")
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadEventsWithFilter(filter: EventFilterDTO) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                Log.d("EventViewModel","Antes de eventFilter: " + filter.beforeDate.toString())
                Log.d("EventViewModel", "Despues de eventFilter: " + filter.afterDate.toString())


                val eventsResult = repository.filterEvents(filter)
                _events.value = eventsResult


            } catch (e: Exception) {
                Log.e("EventViewModel", "Error filtrando eventos: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createEvent(event: EventDTO, onResult: (EventDTO?) -> Unit) {
        viewModelScope.launch {
            try {
                val created = repository.postEvent(event)
                onResult(created)
            } catch(e: Exception) {
                Log.e("EventViewModel", "Error creando el evento: ${e.message}")
                onResult(null)
            }
        }
    }

    fun updateEvent(event: EventDTO, onResult: (EventDTO?) -> Unit) {
        viewModelScope.launch {
            try {
                val created = repository.updateEvent(event)
                onResult(created)
            }catch(e: Exception) {
                Log.e("EventViewModel", "Error al editar el evento: ${e.message}")
                onResult(null)
            }
        }
    }

    fun uploadImagenEvento(eventoId: Long, uri: Uri, nombreImagen: String, onResult: (String?) -> Unit) {
        viewModelScope.launch {
            try {
                val imageName = repository.uploadImagenEvento(eventoId, uri, nombreImagen)
                onResult(imageName)
            } catch (e: Exception) {
                Log.e("EventViewModel", "Error subiendo imagen", e)
                onResult(null)
            }
        }
    }
    fun apuntarUsuario(idEvento : Long, idUsuario : Long){
        viewModelScope.launch {
            try {
                repository.apuntarUsuario(idEvento, idUsuario)
            } catch (e: Exception) {
                Log.e("EventViewModel", "Error apuntando usuario", e)
            }
        }

    }
}