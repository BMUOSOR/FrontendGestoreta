package com.example.frontendgestoreta.data.models

import java.time.LocalDate
import java.time.OffsetDateTime

data class FallaDTO(
    val idFalla: Long,
    val createdAt: OffsetDateTime,
    val nombre: String,
    val fechaCreacion: LocalDate,
    val idGestor: Long,
    val acta: String?,
    val escudo: String?,
    val direccion: String?,
    val descripcion: String?
)