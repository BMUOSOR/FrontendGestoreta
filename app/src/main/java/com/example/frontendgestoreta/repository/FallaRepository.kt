package com.example.frontendgestoreta.repository

import com.example.frontendgestoreta.data.api.ApiService
import com.example.frontendgestoreta.data.api.RetrofitClient
import com.example.frontendgestoreta.data.models.FallaDTO
import retrofit2.Call

class FallaRepository(private val apiService: ApiService) {

    suspend fun getAllFallas() : List<FallaDTO> {
        return apiService.getAllFallas()
    }

    suspend fun getFallaById(id: Long): FallaDTO? {
        val todas = apiService.getAllFallas()
        return todas.find { it.idFalla == id }
    }

}