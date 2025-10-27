package com.example.frontendgestoreta.repository

import com.example.frontendgestoreta.data.api.ApiService
import com.example.frontendgestoreta.data.api.RetrofitClient
import com.example.frontendgestoreta.data.models.MemberDTO
import com.example.frontendgestoreta.data.models.MemberRequestDTO
import retrofit2.Call

// Ejemplo en MemberRepository.kt
class MemberRepository(private val apiService: ApiService) {


    suspend fun getAllMembers() : List<MemberDTO> {
        // Llama a la función suspendida de ApiService directamente
        return apiService.getAllUsers()
    }

    suspend fun getAllRequests() : List<MemberRequestDTO> {
        return apiService.getAllRequests()
    }
}