package com.example.frontendgestoreta.repository

import com.example.frontendgestoreta.data.api.ApiService
import com.example.frontendgestoreta.data.api.RetrofitClient
import com.example.frontendgestoreta.data.models.EventDTO
import com.example.frontendgestoreta.data.models.SuscripcionDTO

class SuscripcionRepository(private val apiService: ApiService) {

    suspend fun getSuscriptionsFromUser(idCuenta : Long) : List<SuscripcionDTO> {
        return apiService.getSuscripcionesFromUser(idCuenta)
    }

}