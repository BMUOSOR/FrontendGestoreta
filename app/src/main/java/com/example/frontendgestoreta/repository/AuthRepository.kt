package com.example.frontendgestoreta.repository

import android.util.Log
import com.example.frontendgestoreta.data.api.RetrofitClient.apiService
import com.example.frontendgestoreta.data.models.GestorDTO
import com.example.frontendgestoreta.data.models.MemberDTO

class AuthRepository {
    suspend fun loginAsUser() : MemberDTO {
        val users = apiService.getAllUsers()

        Log.d("AuthRepository", "Usuarios obtenidos: ${users.size}")
        users.forEachIndexed { index, user ->
            Log.d("AuthRepository", "Usuario $index: id=${user.idUsuario}, dni=${user.dni}, nombre=${user.nombre}")
        }

        val user = users.firstOrNull()
        Log.d("AuthRepository", "Usuario encontrado: ${user?.dni}")

        if (user != null) {
            Log.d("AuthRepository", "✅ USUARIO ENCONTRADO: id=${user.idUsuario}, dni=${user.dni}, nombre=${user.nombre}")
            return user
        } else {
            Log.e("AuthRepository", "❌ NO se encontró usuario con id=1")
            throw Exception("No hay usuario con id=1")
        }
    }
    
    suspend fun loginAsGestor() : GestorDTO{
        return apiService.getFirstGestor()
    }
}