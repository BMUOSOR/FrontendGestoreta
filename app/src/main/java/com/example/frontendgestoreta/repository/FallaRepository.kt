package com.example.frontendgestoreta.repository

import com.example.frontendgestoreta.data.api.ApiService
import com.example.frontendgestoreta.data.api.RetrofitClient
import com.example.frontendgestoreta.data.models.FallaDTO
import retrofit2.Call

class FallaRepository(private val apiService: ApiService) {

    suspend fun getAllFallas() : List<FallaDTO> {
        return apiService.getAllFallas()
    }
    suspend fun updateFalla(idFalla: Long, fallaDTO: FallaDTO): FallaDTO {
        return apiService.updateFalla(idFalla, fallaDTO)
    }

    suspend fun getFallaById(idFalla: Long): FallaDTO {

        val allFallas = apiService.getAllFallas()
        return allFallas.first { it.idFalla == idFalla }
    }

    suspend fun getEscudoFalla(idFalla: Long): ByteArray? {
        val response = apiService.getEscudoFalla(idFalla)
        return if (response.isSuccessful) {
            response.body()?.bytes()
        } else {
            null
        }
    }

}