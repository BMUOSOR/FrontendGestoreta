package com.example.frontendgestoreta.data.models

import java.time.LocalDate
import java.time.OffsetDateTime

data class MemberDTO(
    val idUsuario: Long,
    val createdAt: OffsetDateTime,
    val fechaNac: LocalDate,
    val nombre: String?,
    val apellidos: String?,
    val dni: String?,
    val idFalla: Long,
    var fotoPerfil: String?
)