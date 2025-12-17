package com.example.frontendgestoreta.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontendgestoreta.data.api.RetrofitClient
import com.example.frontendgestoreta.data.models.GestorDTO
import com.example.frontendgestoreta.data.models.MemberDTO
import com.example.frontendgestoreta.data.models.SuscripcionDTO
import com.example.frontendgestoreta.repository.AuthRepository
import com.example.frontendgestoreta.repository.MemberRepository
import com.example.frontendgestoreta.repository.SuscripcionRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

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

    private var _pfp = MutableStateFlow(byteArrayOf(0))
    var pfp : StateFlow<ByteArray> = _pfp
    private val subscriptionRepo = SuscripcionRepository(RetrofitClient.apiService)


    // Estados de datos
    private val _suscripciones = MutableStateFlow<List<SuscripcionDTO>>(emptyList())

    val suscripciones: MutableStateFlow<List<SuscripcionDTO>> = _suscripciones

    private val memberRepo = MemberRepository(RetrofitClient.apiService)

    fun loginAsUser(onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading.value = true
            error.value = null
            try {
                val user = repository.loginAsUser()
                _currentUser.value = user
                getUserImage(userId = user.idUsuario)
                getSubsFromCuenta(user.idUsuario)
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

    fun setPFP(bytes : ByteArray) {
        Log.d("AuthViewModel", "${bytes.size}")
        _pfp.value = bytes
    }
    fun updateProfilePicture(bytes: ByteArray, part: MultipartBody.Part) {
        viewModelScope.launch {
            try {
                memberRepo.uploadUserImage(currentUser.value!!.idUsuario,part)
                val newImg = memberRepo.getUserImage(currentUser.value!!.idUsuario)
                _pfp.value = newImg

            } catch(e : Exception) {
                Log.e("AuthViewModel", "Error al subir la imagen de perfil: ${e.message}")
            }
        }
    }

    fun getUserImage(userId : Long){
        viewModelScope.launch {
            try {
                val pfpResult = memberRepo.getUserImage(userId)
                _pfp.value = pfpResult
                Log.d("AuthViewModel","${pfpResult.size}")
            } catch(e : Exception) {
                Log.e("AuthViewModel", "Error al obtener la imagen del miembro: ${e.message}")
            }
        }
    }

    fun getSubsFromCuenta(idUsuario: Long) {
        viewModelScope.launch {
            try {
                val subscritionsResult = subscriptionRepo.getSuscriptionsFromUser(idUsuario)
                _suscripciones.value = subscritionsResult

            } catch(e: Exception) {
                Log.e("SuscripcionViewModel", "Error al obtener suscripciones: ${e.message}")
            }
        }
    }
}