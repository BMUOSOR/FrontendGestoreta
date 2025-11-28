package com.example.frontendgestoreta.data.api

import com.example.frontendgestoreta.data.models.EventDTO
import com.example.frontendgestoreta.data.models.EventFilterDTO
import com.example.frontendgestoreta.data.models.FallaDTO
import com.example.frontendgestoreta.data.models.GestorDTO
import com.example.frontendgestoreta.data.models.MemberDTO
import com.example.frontendgestoreta.data.models.MemberRequestDTO
import com.example.frontendgestoreta.data.models.TagDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @GET ("gestor/1")
    suspend fun getFirstGestor(): GestorDTO

    @GET("usuario/getAll")
    suspend fun getAllUsers(): List<MemberDTO>

    @GET("solicitud/getAll")
    suspend fun getAllRequests(): List<MemberRequestDTO>

    @GET("usuario/getFromFalla/{idFalla}")
    suspend fun getUsersFromFalla(@Path("idFalla") idFalla: Long): List<MemberDTO>

    @GET("falla/getAll")
    suspend fun getAllFallas(): List<FallaDTO>

    @GET("evento/getAll")
    suspend fun getAllEvents(): List<EventDTO>

    @POST("evento/filterEvents")
    suspend fun filterEvents(@Body filter : EventFilterDTO) : List<EventDTO>

    @POST("evento/postEvento")
    suspend fun postEvent(@Body event : EventDTO)

    @PUT("evento/{idEvento}/update")
    suspend fun updateEvent(
        @Path("idEvento") idEvento: Long,
        @Body event: EventDTO
    )

    @GET("etiqueta/getAll")
    suspend fun getAllTags() : List<TagDTO>

    @GET("evento/getFromFalla/{idFalla}")
    suspend fun getEventosFromFalla(@Path("idFalla") fallaId : Long) : List<EventDTO>

}