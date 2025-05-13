package com.example.solvr.repository

import com.example.solvr.models.PlafonDTO
import retrofit2.Call

class PlafonRepository {
    fun getAllPlafon(): Call<PlafonDTO.ResponseAllPlafon> {
        return ApiClient.plafonService.getAllPlafon()
    }
}