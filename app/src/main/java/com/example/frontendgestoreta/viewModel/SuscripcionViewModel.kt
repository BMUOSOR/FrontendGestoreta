package com.example.frontendgestoreta.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontendgestoreta.data.api.RetrofitClient
import com.example.frontendgestoreta.data.models.EventDTO
import com.example.frontendgestoreta.data.models.FallaDTO
import com.example.frontendgestoreta.data.models.MemberDTO
import com.example.frontendgestoreta.data.models.SuscripcionDTO
import com.example.frontendgestoreta.repository.EventRepository
import com.example.frontendgestoreta.repository.FallaRepository
import com.example.frontendgestoreta.repository.SuscripcionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SuscripcionViewModel : ViewModel() {
    private val subscriptionRepo = SuscripcionRepository(RetrofitClient.apiService)


    // Estados de datos
    private val _suscripciones = MutableStateFlow<List<SuscripcionDTO>>(emptyList())

    val suscripciones: MutableStateFlow<List<SuscripcionDTO>> = _suscripciones


    fun getFromCuenta(member: MemberDTO) {
        viewModelScope.launch {
            try {
                val subscritionsResult = subscriptionRepo.getSuscriptionsFromUser(member.idUsuario)
                _suscripciones.value = subscritionsResult

            } catch(e: Exception) {
                Log.e("SuscripcionViewModel", "Error al obtener suscripciones: ${e.message}")
            }
        }
    }
}