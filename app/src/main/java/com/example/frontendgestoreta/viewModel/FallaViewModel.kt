package com.example.frontendgestoreta.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontendgestoreta.data.api.RetrofitClient
import com.example.frontendgestoreta.data.models.FallaDTO
import com.example.frontendgestoreta.repository.FallaRepository
import com.example.frontendgestoreta.repository.MemberRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FallaViewModel : ViewModel() {

    private val repository = FallaRepository(RetrofitClient.apiService)
    private val memberRepository = MemberRepository(RetrofitClient.apiService)

    // Estado de la lista de fallas
    private val _fallas = MutableStateFlow<List<FallaDTO>>(emptyList())
    val fallas: StateFlow<List<FallaDTO>> = _fallas.asStateFlow()

    // Estado de carga (Loading)
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadFallas() {
        viewModelScope.launch {
            _isLoading.value = true // 1. Activar carga
            try {
                // 2. Cargar datos
                _fallas.value = repository.getAllFallas()
            } catch (e: Exception) {
                Log.e("FallaViewModel", "Error al cargar datos: ${e.message}", e)
                // Aquí podrías manejar un estado de error si quisieras
            } finally {
                _isLoading.value = false // 3. Desactivar carga siempre (haya error o no)
            }
        }
    }

    // Necesario porque tu UI llama a viewModel.getMemberCount
    fun getMemberCount(idFalla: Long, onResult: (Int) -> Unit) {
        viewModelScope.launch {
            try {
                val members = memberRepository.getMembersFromFalla(idFalla)
                onResult(members.size)
            } catch (e: Exception) {
                onResult(0)
            }
        }
    }
}