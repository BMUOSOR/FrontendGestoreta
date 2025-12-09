package com.example.frontendgestoreta.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontendgestoreta.data.api.RetrofitClient
import com.example.frontendgestoreta.data.models.EventDTO
import com.example.frontendgestoreta.data.models.MemberDTO
import com.example.frontendgestoreta.data.models.MemberRequestDTO
import com.example.frontendgestoreta.repository.MemberRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MemberViewModel : ViewModel() {

    private val repository = MemberRepository(RetrofitClient.apiService)
    private val DEFAULT_FALLA_ID = 1;
    private val _members = MutableStateFlow<List<MemberDTO>>(emptyList())
    val members: StateFlow<List<MemberDTO>> = _members

    private val _requests = MutableStateFlow<List<MemberRequestDTO>>(emptyList())
    val requests: StateFlow<List<MemberRequestDTO>> = _requests

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    fun loadMembers() {
        viewModelScope.launch {
            try {
                Log.d("MembersViewModel", "Iniciando carga de datos...")
                val membersResult = repository.getMembersFromFalla(1)
                val requestsResult = repository.getRequestsFromFalla(1)

                _members.value = membersResult
                _requests.value = requestsResult
                Log.d("MembersViewModel", "Datos cargados: ${membersResult.size} miembros, ${requestsResult.size} solicitudes.")
            } catch (e: Exception) {
                val errorMsg = "Error al cargar datos: ${e.message}"
                println(errorMsg)
                _message.value = errorMsg
            }
        }
    }
    fun acceptRequest(id: Long) {
        viewModelScope.launch {
            try {
                _message.value = "Aceptando solicitud..."
                repository.acceptRequest(id)
                _message.value = "Solicitud aceptada correctamente"
                loadMembers()
            } catch (e: Exception) {
                _message.value = "Error al aceptar: ${e.message}"
            }
        }
    }
    fun rejectRequest(requestId: Long) {
        viewModelScope.launch {
            try {
                // 1. Llamar a la API para rechazar (eliminar) la solicitud
                repository.rejectRequest(requestId) // Asumiendo este método en el Repository
                _message.value = "Solicitud $requestId rechazada."
                // 2. Recargar los datos
                loadMembers()
            } catch (e: Exception) {
                val errorMsg = "Error al rechazar solicitud: ${e.message}"
                println(errorMsg)
                _message.value = errorMsg
            }
        }
    }
    fun deleteMember(member: MemberDTO) {
        viewModelScope.launch {
            try {
                _message.value = "Eliminando miembro..."
                member.idUsuario?.let { repository.deleteMember(it) }
                _message.value = "Miembro eliminado correctamente"
                loadMembers()
            } catch (e: Exception) {
                _message.value = "Error: ${e.message}"
            }
        }
    }

    fun updateUsuario(member: MemberDTO) {
        viewModelScope.launch {
            try {
                repository.updateUsuario(member)
            } catch(e: Exception) {
                Log.e("MemberViewModel", "Error al editar el miembro: ${e.message}")
            }
        }
    }
}