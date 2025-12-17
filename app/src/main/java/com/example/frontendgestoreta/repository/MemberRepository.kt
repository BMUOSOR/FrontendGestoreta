package com.example.frontendgestoreta.repository

import android.util.Log
import com.example.frontendgestoreta.data.api.ApiService
import com.example.frontendgestoreta.data.api.RetrofitClient
import com.example.frontendgestoreta.data.models.EventDTO
import com.example.frontendgestoreta.data.models.MemberDTO
import com.example.frontendgestoreta.data.models.MemberRequestDTO
import okhttp3.MultipartBody
import java.time.LocalDate
import java.time.OffsetDateTime

class MemberRepository(private val apiService: ApiService) {

    suspend fun getAllMembers() : List<MemberDTO> {
        return apiService.getAllUsers()
    }

    suspend fun getAllRequests() : List<MemberRequestDTO> {
        return apiService.getAllRequests()
    }
    suspend fun getMembersFromFalla(idFalla: Long) : List<MemberDTO> {
        return apiService.getUsersFromFalla(idFalla)
    }
    suspend fun getRequestsFromFalla(idFalla: Long) : List<MemberRequestDTO> {
        return apiService.getRequestsFromFalla(idFalla)
    }
    suspend fun deleteMember(idUsuario: Long) {
        try{
        val usuarioExistente = apiService.getById(idUsuario)
        val usuarioParaDesvincular = usuarioExistente.copy(
            idFalla = null,
            createdAt = null
        )
        apiService.updateUsuario(idUsuario, usuarioParaDesvincular)
        } catch (e: Exception) {
            throw Exception("Error al desvincular miembro: ${e.message}")
        }
    }

    suspend fun rejectRequest(idSolicitud: Long) {
        apiService.deleteRequest(idSolicitud)
    }
    suspend fun postRequest(request: MemberRequestDTO): MemberRequestDTO {
        return apiService.postRequest(request)
    }
    suspend fun acceptRequest(idSolicitud: Long) {
        try {
            val request = apiService.getRequestById(idSolicitud)
            val dni = request.dni ?: throw Exception("Falta DNI")
            val idFalla = request.idFalla ?: throw Exception("Falta falla")


            val usuarios = apiService.getAllUsers()
            val usuarioExistente = usuarios.find { it.dni == dni }
                ?: throw Exception("Usuario con DNI $dni no encontrado")


            val usuarioActualizado = usuarioExistente.copy(
                idFalla = idFalla,
                createdAt = null
            )
            apiService.updateMember(usuarioExistente.idUsuario!!, usuarioActualizado)


            apiService.deleteRequest(idSolicitud)

        } catch (e: Exception) {
            throw Exception("Error al aceptar solicitud: ${e.message}")
        }
    }

    suspend fun updateUsuario(member: MemberDTO) {
        apiService.updateUsuario(member.idUsuario, member)
    }

    suspend fun getUserImage(idUsuario : Long) : ByteArray {
        return apiService.getProfileImageOfUser(idUsuario).body()?.bytes() ?: ByteArray(0)
    }

    suspend fun deleteUserImage(idUsuario : Long) {
        apiService.deleteProfileImageOfUser(idUsuario)
    }

    suspend fun uploadUserImage(idUsuario: Long, file : MultipartBody.Part) {
        val response = apiService.uploadFoto(idUsuario,file)
        Log.d("AuthViewModel", "Respuesta: $response")
    }

}