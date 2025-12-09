package com.example.frontendgestoreta.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontendgestoreta.data.api.RetrofitClient
import com.example.frontendgestoreta.data.models.FallaDTO
import com.example.frontendgestoreta.repository.FallaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class FallaSettingsUiState(
    val falla: FallaDTO? = null,
    val isLoading: Boolean = false,
    val saveSuccessful: Boolean = false,
    val errorMessage: String? = null
)

class FallaSettingsViewModel(
) : ViewModel() {
    private val fallaRepository = FallaRepository(RetrofitClient.apiService)
    private val fallaId: Long = 1
    private val _uiState = MutableStateFlow(FallaSettingsUiState())
    val uiState: StateFlow<FallaSettingsUiState> = _uiState

    init {
        loadFallaDetails()
    }

    private fun loadFallaDetails() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            try {

                val falla = fallaRepository.getFallaById(fallaId)
                _uiState.update { it.copy(falla = falla, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error al cargar los detalles de la Falla: ${e.message}"
                    )
                }
            }
        }
    }

    fun updateFallaField(
        nombre: String = uiState.value.falla?.nombre ?: "",
        descripcion: String? = uiState.value.falla?.descripcion,
        direccion: String? = uiState.value.falla?.direccion,
        acta:   String? = uiState.value.falla?.acta
    ) {
        val currentFalla = _uiState.value.falla ?: return

        val updatedFalla = currentFalla.copy(
            nombre = nombre,
            descripcion = descripcion,
            direccion = direccion,
            acta = acta
        )

        _uiState.update { it.copy(falla = updatedFalla, saveSuccessful = false) }
    }

    // Función para guardar los cambios
    fun saveFallaDetails() {
        val fallaToSave = _uiState.value.falla ?: return

        // ⭐⭐⭐ INICIO DEL CÓDIGO DE DEPURACIÓN (FORZAR FECHAS) ⭐⭐⭐

        // 1. Forzar createdAt a la fecha y hora actual
        val fixedCreatedAt = OffsetDateTime.now()

        // 2. Forzar fechaCreacion a la fecha actual
        val fixedFechaCreacion = LocalDate.now()

        // 3. Crear una copia del DTO con las fechas forzadas, manteniendo los IDs y campos de texto
        val debugFalla = fallaToSave.copy(
            createdAt = fixedCreatedAt,
            fechaCreacion = fixedFechaCreacion
        )



        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            try {
                val savedFalla = fallaRepository.updateFalla(debugFalla.idFalla, debugFalla)

                _uiState.update {
                    it.copy(
                        falla = savedFalla, // Refrescar con la respuesta del servidor
                        isLoading = false,
                        saveSuccessful = true
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        // Mostrar un mensaje específico si la falla es 400
                        errorMessage = "Error al guardar (400 Bad Request): ${e.message}. El backend necesita configurar Jackson (jackson-datatype-jsr310) para fechas de Java.",
                        saveSuccessful = false
                    )
                }
            }
        }
    }

    fun clearSaveStatus() {
        _uiState.update { it.copy(saveSuccessful = false) }
    }
}