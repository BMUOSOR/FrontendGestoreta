// com.example.frontendgestoreta.data.api/RetrofitClient.kt
package com.example.frontendgestoreta.data.api

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.OffsetDateTime
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.ZonedDateTime

object RetrofitClient {
    private const val BASE_URL = "http://10.38.104.164:8080/api/"

    // 1. ADAPTADOR PARA OffsetDateTime (createdAt)
    private val offsetDateTimeDeserializer = JsonDeserializer<OffsetDateTime> { json, _, _ ->
        // Jackson envía un string en formato ISO 8601, lo parseamos directamente
        OffsetDateTime.parse(json.asString, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }

    // 2. ADAPTADOR PARA LocalDate (fechaNac)
    private val localDateDeserializer = JsonDeserializer<LocalDate> { json, _, _ ->
        // Para LocalDate, usamos el formato ISO local, que es "yyyy-MM-dd"
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        LocalDate.parse(json.asString, formatter)
    }

    // 3. Configurar Gson
    private val customGson = GsonBuilder()
        // Registrar el adaptador de OffsetDateTime
        .registerTypeAdapter(OffsetDateTime::class.java, offsetDateTimeDeserializer)
        // Registrar el adaptador de LocalDate
        .registerTypeAdapter(LocalDate::class.java, localDateDeserializer)
        .create()

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            // 4. Aplicar el Gson con los adaptadores
            .addConverterFactory(GsonConverterFactory.create(customGson))
            .build()
            .create(ApiService::class.java)
    }
}