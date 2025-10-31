package com.example.frontendgestoreta.data.api

import com.example.frontendgestoreta.data.models.MemberDTO
import com.example.frontendgestoreta.data.models.MemberRequestDTO
import com.example.frontendgestoreta.data.models.FallaDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {


    @GET("usuario/getAll")
    suspend fun getAllUsers(): List<MemberDTO>

    @GET("solicitud/getAll")
    suspend fun getAllRequests(): List<MemberRequestDTO>

    @GET("usuario/getFromFalla/{idFalla}")
    suspend fun getUsersFromFalla(@Path("idFalla") idFalla: Long): List<MemberDTO>

    @GET("falla/getAll")
    suspend fun getAllFallas(): List<FallaDTO>
}