package com.example.frontendgestoreta.repository

import com.example.frontendgestoreta.data.api.ApiService
import com.example.frontendgestoreta.data.api.RetrofitClient.apiService
import com.example.frontendgestoreta.data.models.EventDTO

class EventRepository (private val apiService: ApiService){
    suspend fun getAllEvents(): List<EventDTO> {
        return apiService.getAllEvents()
    }
}