package com.example.frontendgestoreta.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontendgestoreta.data.api.RetrofitClient
import com.example.frontendgestoreta.data.models.FallaDTO
import com.example.frontendgestoreta.repository.FallaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FallaViewModel : ViewModel() {

    private val repository = FallaRepository(RetrofitClient.apiService)

    private val _fallas = MutableStateFlow<List<FallaDTO>>(emptyList())
    val fallas: StateFlow<List<FallaDTO>> = _fallas

    fun loadFallas() {
        viewModelScope.launch {
            try {
                _fallas.value = repository.getAllFallas()

            } catch (e: Exception) {

                Log.e("FallaViewModel", "Error al cargar datos: ${e.message}", e)
            }
        }
    }
}