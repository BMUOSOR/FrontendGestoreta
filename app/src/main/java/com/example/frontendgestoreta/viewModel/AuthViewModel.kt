package com.example.frontendgestoreta.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontendgestoreta.data.models.GestorDTO
import com.example.frontendgestoreta.data.models.MemberDTO
import com.example.frontendgestoreta.repository.AuthRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val repository = AuthRepository()

    private val _currentUser = MutableStateFlow<MemberDTO?>(null)
    private val _currentUserGestor = MutableStateFlow<GestorDTO?>(null)
    val currentUser: StateFlow<MemberDTO?> = _currentUser
    val currentUserGestor: StateFlow<GestorDTO?> = _currentUserGestor


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
                _currentUser.value = user
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
                _currentUserGestor.value = gestor
                onSuccess()
            } catch (e: Exception) {
                error.value = e.message
            } finally {
                isLoading.value = false
            }
        }
    }
}