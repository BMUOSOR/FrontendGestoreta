// com.example.frontendgestoreta.data.api/RetrofitClient.kt
package com.example.frontendgestoreta.data.api

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.OffsetDateTime
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object RetrofitClient {
    private const val BASE_URL = "http://192.168.1.31:8080/api/"

    // 1. ADAPTADOR PARA OffsetDateTime (createdAt)
    private val offsetDateTimeDeserializer = JsonDeserializer<OffsetDateTime> { json, _, _ ->
        // Jackson envía un string en formato ISO 8601, lo parseamos directamente
        OffsetDateTime.parse(json.asString, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }

    // 2. ADAPTADOR PARA LocalDate (fechaNac)
    private val localDateDeserializer = JsonDeserializer<LocalDate> { json, _, _ ->
        LocalDate.parse(json.asString, DateTimeFormatter.ISO_LOCAL_DATE)
    }

    // 3. ADAPTADOR LocalDate (ENVIAR "yyyy-MM-dd")
    private val localDateSerializer = JsonSerializer<LocalDate> { src, _, _ ->
        JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE))
    }

    // 4. Configurar Gson
    private val customGson = GsonBuilder()
        .registerTypeAdapter(OffsetDateTime::class.java, offsetDateTimeDeserializer)
        .registerTypeAdapter(LocalDate::class.java, localDateDeserializer)
        .registerTypeAdapter(LocalDate::class.java, localDateSerializer)
        .create()

    // 5. Retrofit
    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(customGson))
            .build()
            .create(ApiService::class.java)
    }
}
