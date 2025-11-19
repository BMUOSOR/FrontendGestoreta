package com.example.frontendgestoreta.data.models

import java.time.OffsetDateTime

data class GestorDTO (
    val id: Long?,
    val createdAt: OffsetDateTime?,
    val cuenta: Long?,
    val falla: Long?
)