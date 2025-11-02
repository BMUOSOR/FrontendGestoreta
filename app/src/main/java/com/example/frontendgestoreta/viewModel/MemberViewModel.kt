package com.example.frontendgestoreta.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontendgestoreta.data.api.RetrofitClient
import com.example.frontendgestoreta.data.models.MemberDTO
import com.example.frontendgestoreta.data.models.MemberRequestDTO
import com.example.frontendgestoreta.repository.MemberRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MemberViewModel : ViewModel() {

    private val repository = MemberRepository(RetrofitClient.apiService)

    private val _members = MutableStateFlow<List<MemberDTO>>(emptyList())
    val members: StateFlow<List<MemberDTO>> = _members

    private val _requests = MutableStateFlow<List<MemberRequestDTO>>(emptyList())
    val requests: StateFlow<List<MemberRequestDTO>> = _requests

    fun loadMembers() {
        viewModelScope.launch {
            try {
                Log.d("MembersViewModel", "Iniciando carga de datos...")
                val membersResult = repository.getAllMembers()
                val requestsResult = repository.getAllRequests()

                _members.value = membersResult
                _requests.value = requestsResult
                Log.d("MembersViewModel", "Datos cargados: ${membersResult.size} miembros, ${requestsResult.size} solicitudes.")
            } catch (e: Exception) {
                // MANEJO DE ERRORES:
                println("Error al cargar datos: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}