
package com.example.frontendgestoreta.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontendgestoreta.data.api.RetrofitClient
import com.example.frontendgestoreta.data.models.MemberRequestDTO
import com.example.frontendgestoreta.repository.MemberRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.time.OffsetDateTime


class FallaDetailViewModel : ViewModel() {

    private val repository = MemberRepository(RetrofitClient.apiService)
    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    fun sendJoinRequest(fallaId: Long, nombreCompleto: String, dni: String, motivo: String) {
        Log.d("FallaDetailVM", "Enviando solicitud: falla=$fallaId, nombre=$nombreCompleto, dni=$dni")

        viewModelScope.launch {
            try {
                val requestDTO = MemberRequestDTO(
                    idSolicitud = null,
                    createdAt = null,
                    idFalla = fallaId,
                    contenido = nombreCompleto,
                    aprobada = false,
                    idGestor = null,
                    motivo = motivo,
                    dni = dni
                )

                Log.d("FallaDetailVM", "DTO creado: $requestDTO")

                // Esta línea SÍ se ejecuta
                val response = repository.postRequest(requestDTO)

                Log.d("FallaDetailVM", "Solicitud enviada con éxito: $response")
                _message.value = "¡Solicitud enviada con éxito!"

            } catch (e: HttpException) {
                Log.e("FallaDetailVM", "Error HTTP: ${e.code()} - ${e.message()}")
                _message.value = "Error del servidor: ${e.message()}"
            } catch (e: Exception) {
                Log.e("FallaDetailVM", "Error inesperado: ${e.message}", e)
                _message.value = "Error: ${e.message}"
            }
        }
    }

    fun setMessage(msg: String) {
        _message.value = msg
    }

    fun messageShown() {
        _message.value = null
    }
}