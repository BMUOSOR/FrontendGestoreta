package com.example.frontendgestoreta.data.api

import com.example.frontendgestoreta.data.models.EventDTO
import com.example.frontendgestoreta.data.models.EventFilterDTO
import com.example.frontendgestoreta.data.models.FallaDTO
import com.example.frontendgestoreta.data.models.GestorDTO
import com.example.frontendgestoreta.data.models.MemberDTO
import com.example.frontendgestoreta.data.models.MemberRequestDTO
import com.example.frontendgestoreta.data.models.SuscripcionDTO
//import com.example.frontendgestoreta.data.models.TagDTO
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @GET("asistencia/apuntarUsuario")
    suspend fun apuntarUsuario(@Path("idEvento") idEvento: Long, @Path("idUsuario") idUsuario: Long)

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


    @GET("solicitud/getFromFalla/{idFalla}")
    suspend fun getRequestsFromFalla(@Path("idFalla") idFalla: Long): List<MemberRequestDTO>


    @GET("solicitud/getById/{idSolicitud}")
    suspend fun getRequestById(@Path("idSolicitud") idSolicitud: Long): MemberRequestDTO


    @DELETE("usuario/removeUsuario/{idUsuario}")
    suspend fun deleteMember(@Path("idUsuario") idUsuario: Long): MemberDTO

    @DELETE("solicitud/deleteSolicitud/{idSolicitud}")
    suspend fun deleteRequest(@Path("idSolicitud") idSolicitud: Long): MemberRequestDTO

    @POST("usuario/postUsuario")
    suspend fun createMember(@Body member: MemberDTO): MemberDTO

    @POST("solicitud/postSolicitud")
    suspend fun postRequest(@Body solicitud: MemberRequestDTO): MemberRequestDTO

    @PUT("usuario/updateUsuario/{idUsuario}")
    suspend fun updateMember(
        @Path("idUsuario") idUsuario: Long,
        @Body member: MemberDTO
    ): MemberDTO

    @PUT("falla/updateFalla/{idFalla}")
    suspend fun updateFalla(
        @Path("idFalla") idFalla: Long,
        @Body fallaDTO: FallaDTO
    ): FallaDTO
    @GET("usuario/getById/{idUsuario}") // Mapea a /api/usuario/getById/{idUsuario}
    suspend fun getById(@Path("idUsuario") idUsuario: Long): MemberDTO // MemberDTO es UsuarioDTO en el backend


    @POST("evento/filterEvents")
    suspend fun filterEvents(@Body filter : EventFilterDTO) : List<EventDTO>

    @POST("evento/postEvento")
    suspend fun postEvent(@Body event : EventDTO)

    @PUT("evento/{idEvento}/update")
    suspend fun updateEvent(
        @Path("idEvento") idEvento: Long,
        @Body event: EventDTO
    )


    @PUT("usuario/updateUsuario/{idUsuario}")
    suspend fun updateUsuario(
        @Path("idUsuario") idUsuario: Long,
        @Body member: MemberDTO
    )

    @GET("evento/getFromFalla/{idFalla}")
    suspend fun getEventosFromFalla(@Path("idFalla") fallaId : Long) : List<EventDTO>

    @GET("solicitud/getFromUser/{idCuenta}")
    suspend fun getSuscripcionesFromUser(@Path("idCuenta") idCuenta : Long) : List<SuscripcionDTO>

}