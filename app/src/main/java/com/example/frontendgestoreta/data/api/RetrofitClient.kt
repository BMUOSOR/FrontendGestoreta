package com.example.frontendgestoreta.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import java.time.OffsetDateTime
import java.time.LocalDate
import java.time.format.DateTimeFormatter


object RetrofitClient {
    private const val BASE_URL = "http://192.168.0.13:8080/api/" // 10.0.2.2

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}