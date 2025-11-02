package com.example.frontendgestoreta.data.models
import java.time.LocalDate
import java.time.OffsetDateTime

data class EventDTO (
    val idEvento: Long? = null,
    val createdAt: OffsetDateTime? = null,
    val fecha: LocalDate? = null,
    val ubicacion: String? = null,
    val idFalla: Long? = null,
    val idAnuncio: Long? = null,
    val idEtiqueta: Long? = null,
    val titulo: String? = null,
    val descripcion: String? = null,
    val maxPersonas: Long? = null,
)