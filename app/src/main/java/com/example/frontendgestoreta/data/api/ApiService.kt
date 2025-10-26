package com.example.frontendgestoreta.data.api

import com.example.frontendgestoreta.data.models.MemberDTO
import com.example.frontendgestoreta.data.models.MemberRequestDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("usuario/getAll")
    fun getAllUsers(): Call<List<MemberDTO>>

    @GET("solicitud/getAll")
    fun getAllRequests(): Call<List<MemberRequestDTO>>

    @GET("usuario/getFromFalla/{idFalla}")
    fun getUsersFromFalla(@Path("idFalla") idFalla: Long): Call<List<MemberDTO>>
}