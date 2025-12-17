package com.example.frontendgestoreta.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.frontendgestoreta.data.api.ApiService
import com.example.frontendgestoreta.data.api.RetrofitClient.apiService
import com.example.frontendgestoreta.data.models.EventDTO
import com.example.frontendgestoreta.data.models.MemberRequestDTO
import com.example.frontendgestoreta.data.models.EventFilterDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.http.Part
import retrofit2.http.Path
import java.io.File

class EventRepository (private val apiService: ApiService){
    suspend fun getAllEvents(): List<EventDTO> {
        return apiService.getAllEvents()
    }

    suspend fun filterEvents(eventFilter : EventFilterDTO): List<EventDTO> {
        return apiService.filterEvents(eventFilter)
    }

    suspend fun postEvent(event: EventDTO): EventDTO{
        return apiService.postEvent(event)
    }

    suspend fun updateEvent(event: EventDTO):EventDTO {
        return apiService.updateEvent(event.idEvento!!, event)
    }

    suspend fun getEventsFromFalla(fallaId : Long) : List<EventDTO> {
        return apiService.getEventosFromFalla(fallaId)
    }
    suspend fun apuntarUsuario(idEvento : Long, idUsuario : Long){
        return apiService.apuntarUsuario(idEvento, idUsuario)
    }
    suspend fun postRequest(request: MemberRequestDTO): MemberRequestDTO {
        return apiService.postRequest(request)
    }

    suspend fun uploadImagenEvento(eventoId: Long, uri: Uri, nombreImagen: String): String {
        return withContext(Dispatchers.IO) {
            val file = File(uri.path!!)
            val requestFile = file.asRequestBody("image/jpeg".toMediaType())
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
            val nombreImagenPart = nombreImagen.toRequestBody("text/plain".toMediaType())

            // Llamada al endpoint
            val response = apiService.uploadImagenEvento(eventoId, body, nombreImagenPart)

            // Leer el string devuelto manualmente
            response.string()
        }
    }
}