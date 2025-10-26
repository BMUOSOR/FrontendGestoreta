package com.example.frontendgestoreta.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val repository = MemberRepository()

    private val _members = MutableStateFlow<List<MemberDTO>>(emptyList())
    val members: StateFlow<List<MemberDTO>> = _members

    private val _requests = MutableStateFlow<List<MemberRequestDTO>>(emptyList())
    val requests: StateFlow<List<MemberRequestDTO>> = _requests

    fun loadMembers() {


        repository.getAllMembers().enqueue(object : Callback<List<MemberDTO>> {
            override fun onResponse(call: Call<List<MemberDTO>>, response: Response<List<MemberDTO>>) {
                println("Miembros - Código de respuesta: ${response.code()}") // <-- AGREGAR ESTO
                println("Miembros - Body recibido: ${response.body()}") // <-- AGREGAR ESTO
                if (response.isSuccessful) _members.value = response.body() ?: emptyList()
            }
            override fun onFailure(call: Call<List<MemberDTO>>, t: Throwable) {
                t.printStackTrace() // Si falla, revisa aquí el stack trace para errores de red
            }
        })

        repository.getAllRequests().enqueue(object : Callback<List<MemberRequestDTO>> {
            override fun onResponse(call: Call<List<MemberRequestDTO>>, response: Response<List<MemberRequestDTO>>) {
                println("Código de respuesta: ${response.code()}")
                println("Body recibido: ${response.body()}")
                if (response.isSuccessful) _requests.value = response.body() ?: emptyList()
            }
            override fun onFailure(call: Call<List<MemberRequestDTO>>, t: Throwable) {
                t.printStackTrace()
            }
        })

    }
}
