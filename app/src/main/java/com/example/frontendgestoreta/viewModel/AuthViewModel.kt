package com.example.frontendgestoreta.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontendgestoreta.data.models.GestorDTO
import com.example.frontendgestoreta.data.models.MemberDTO
import com.example.frontendgestoreta.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val repository = AuthRepository()

    var currentUser = mutableStateOf<MemberDTO?>(null)
        private set
    var currentUserGestor = mutableStateOf<GestorDTO?>(null)
        private set

    var isLoading = mutableStateOf(false)
        private set

    var error = mutableStateOf<String?>(null)
        private set

    fun loginAsUser(onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading.value = true
            error.value = null
            try {
                val user = repository.loginAsUser()
                currentUser.value = user
                onSuccess()
            } catch (e: Exception) {
                error.value = e.message
            } finally {
                isLoading.value = false
            }
        }
    }
    fun loginAsGestor(onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading.value = true
            error.value = null
            try {
                val gestor = repository.loginAsGestor()
                currentUserGestor.value = gestor
                onSuccess()
            } catch (e: Exception) {
                error.value = e.message
            } finally {
                isLoading.value = false
            }
        }
    }
}