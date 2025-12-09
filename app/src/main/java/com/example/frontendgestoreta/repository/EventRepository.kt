package com.example.frontendgestoreta.repository

import com.example.frontendgestoreta.data.api.ApiService
import com.example.frontendgestoreta.data.api.RetrofitClient.apiService
import com.example.frontendgestoreta.data.models.EventDTO
import com.example.frontendgestoreta.data.models.MemberRequestDTO
import com.example.frontendgestoreta.data.models.EventFilterDTO

class EventRepository (private val apiService: ApiService){
    suspend fun getAllEvents(): List<EventDTO> {
        return apiService.getAllEvents()
    }

    suspend fun filterEvents(eventFilter : EventFilterDTO): List<EventDTO> {
        return apiService.filterEvents(eventFilter)
    }

    suspend fun postEvent(event: EventDTO) {
        apiService.postEvent(event)
    }

    suspend fun updateEvent(event: EventDTO) {
        apiService.updateEvent(event.idEvento!!, event)
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
}