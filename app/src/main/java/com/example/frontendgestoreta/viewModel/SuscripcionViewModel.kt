package com.example.frontendgestoreta.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontendgestoreta.data.api.RetrofitClient
import com.example.frontendgestoreta.data.models.MemberDTO
import com.example.frontendgestoreta.repository.EventRepository
import com.example.frontendgestoreta.repository.SuscripcionRepository
import kotlinx.coroutines.launch

class SuscripcionViewModel : ViewModel() {
    private val repository = SuscripcionRepository(RetrofitClient.apiService)

    fun getFromCuenta(member: MemberDTO) {
        viewModelScope.launch {
            try {
                repository.getSuscriptionsFromUser(member.idUsuario)
            } catch(e: Exception) {
                Log.e("SuscripcionViewModel", "Error al obtener suscripciones: ${e.message}")
            }
        }
    }
}