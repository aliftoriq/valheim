package com.example.solvr.network

import com.example.solvr.models.LoanDTO
import com.example.solvr.models.LoanSummaryDTO
import com.example.solvr.models.UserDTO
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface UserService {
    @Headers("Content-Type: application/json")
    @GET("user/customer/detail")
    fun getCustomerDetail(): Call<UserDTO.Response>

    @Headers("Content-Type: application/json")
    @POST("user/customer")
    fun login(@Body loginRequest: UserDTO.Request): Call<UserDTO.Response>

    @Headers("Content-Type: application/json")
    @PUT("user/customer")
    fun updateUser(@Body updateRequest: UserDTO.Request): Call<UserDTO.Response>


    @Headers("Content-Type: application/json")
    @POST("user/customer/logout")
    fun logout(): Call<UserDTO.Response>

    @Multipart
    @POST("image/profile")
    fun uploadProfileImage(
        @Part file: MultipartBody.Part
    ): Call<Void>

    @Multipart
    @POST("image/ktp")
    fun uploadKtpImage(
        @Part file: MultipartBody.Part
    ): Call<Void>

    @Multipart
    @POST("image/selfie")
    fun uploadSelfieImage(
        @Part file: MultipartBody.Part
    ): Call<Void>


    // Loan Application
    @Headers("Content-Type: application/json")
    @POST("loan-application")
    fun applyLoan(@Body loanRequest: LoanDTO.Request): Call<LoanDTO.ResponseApplyLoan>

    @Headers("Content-Type: application/json")
    @GET("loan-application/customer/history")
    fun getLoanHistory(): Call<LoanDTO.Response>

    @Headers("Content-Type: application/json")
    @GET("loan-application/summary")
    fun getLoanSummary(): Call<LoanSummaryDTO.Response>

}