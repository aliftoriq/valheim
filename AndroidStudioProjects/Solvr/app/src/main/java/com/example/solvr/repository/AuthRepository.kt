package com.example.solvr.repository

import com.example.solvr.models.AuthDTO
import com.example.solvr.models.AuthDTO.LoginRequest
import com.example.solvr.models.AuthDTO.LoginResponse
import com.example.solvr.models.AuthDTO.RegisterRequest
import com.example.solvr.models.CommonDTO.ResponseTemplate
import retrofit2.Call

class AuthRepository {
    fun loginUser(request: LoginRequest): Call<LoginResponse> {
        return ApiClient.instance.login(request)
    }

    fun registerUser(request: RegisterRequest): Call<ResponseTemplate> {
        return ApiClient.instance.register(request)
    }

    fun saveFirebaseToken(token: String): Call<Any> {
        val request = AuthDTO.FirebaseTokenRequest(token)
        return ApiClient.instance.refreshToken(request)
    }
}
