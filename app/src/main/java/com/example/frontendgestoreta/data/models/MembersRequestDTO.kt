package com.example.frontendgestoreta.data.models

import java.time.OffsetDateTime

data class MemberRequestDTO(
    val idSolicitud: Long?,
    val createdAt: OffsetDateTime?,
    val idFalla: Long?,
    val contenido: String?,
    val aprobada: Boolean?,
    val idGestor: Long?,
    val motivo: String?,
    val dni: Long?
)