package com.example.frontendgestoreta.repository

import com.example.frontendgestoreta.data.api.RetrofitClient
import com.example.frontendgestoreta.data.models.MemberDTO
import com.example.frontendgestoreta.data.models.MemberRequestDTO
import retrofit2.Call

class MemberRepository {
    private val api = RetrofitClient.apiService

    fun getAllMembers(): Call<List<MemberDTO>> = api.getAllUsers()
    fun getAllRequests(): Call<List<MemberRequestDTO>> = api.getAllRequests()
}
