package com.example.solvr.network

import com.example.solvr.models.AuthDTO
import com.example.solvr.models.CommonDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthService {
    @Headers("Content-Type: application/json")
    @POST("auth/login")
    fun login(@Body request: AuthDTO.LoginRequest): Call<AuthDTO.LoginResponse>

    @Headers("Content-Type: application/json")
    @POST("auth/register")
    fun register(@Body request: AuthDTO.RegisterRequest): Call<CommonDTO.ResponseTemplate>

    @Headers("Content-Type: application/json")
    @POST("auth/logout")
    fun logout(): Call<CommonDTO.ResponseTemplate>






    @Headers("Content-Type: application/json")
    @POST("notification")
    fun refreshToken(@Body request: AuthDTO.FirebaseTokenRequest): Call<Any>

}
