package com.example.frontendgestoreta.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontendgestoreta.data.api.RetrofitClient
import com.example.frontendgestoreta.data.models.MemberDTO
import com.example.frontendgestoreta.data.models.MemberRequestDTO
import com.example.frontendgestoreta.repository.MemberRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MembersViewModel : ViewModel() {

    private val repository = MemberRepository(RetrofitClient.apiService)

    private val _members = MutableStateFlow<List<MemberDTO>>(emptyList())
    val members: StateFlow<List<MemberDTO>> = _members

    private val _requests = MutableStateFlow<List<MemberRequestDTO>>(emptyList())
    val requests: StateFlow<List<MemberRequestDTO>> = _requests

    fun loadMembers() {
        viewModelScope.launch {
            try {
                _members.value = repository.getAllMembers()
                _requests.value = repository.getAllRequests()

            } catch (e: Exception) {
                // MANEJO DE ERRORES:
                println("Error al cargar datos: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}