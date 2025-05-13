package com.example.solvr.network

import com.example.solvr.models.AuthDTO
import com.example.solvr.models.CommonDTO
import com.example.solvr.models.PlafonDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface PlafonService {
    @Headers("Content-Type: application/json")
    @GET("plafon/all")
    fun getAllPlafon(): Call<PlafonDTO.ResponseAllPlafon>
}
