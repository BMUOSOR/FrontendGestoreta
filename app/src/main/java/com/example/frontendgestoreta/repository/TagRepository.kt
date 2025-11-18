package com.example.frontendgestoreta.repository

import com.example.frontendgestoreta.data.api.ApiService
import com.example.frontendgestoreta.data.models.TagDTO

class TagRepository(private val apiService: ApiService) {
    suspend fun getAllTags() : List<TagDTO> {
        return apiService.getAllTags()
    }
}