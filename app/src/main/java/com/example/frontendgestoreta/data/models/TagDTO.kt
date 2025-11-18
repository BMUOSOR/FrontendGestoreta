package com.example.frontendgestoreta.data.models

import com.google.gson.annotations.SerializedName
import java.time.OffsetDateTime

data class TagDTO(
    @SerializedName("id")
    val idTag :Long?,
    val createdAt: OffsetDateTime?,
    @SerializedName("descripcion")
    val description: String
)