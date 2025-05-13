package com.example.solvr.repository

import com.example.solvr.models.LoanDTO
import com.example.solvr.models.UserDTO
import retrofit2.Call

class UserRepository {
    fun getUSerDetail(): Call<UserDTO.Response>{
        return ApiClient.userService.getCustomerDetail()
    }

    fun createUser(request: UserDTO.Request): Call<UserDTO.Response> {
        return ApiClient.userService.login(request)
    }

    fun updateUser(request: UserDTO.Request): Call<UserDTO.Response> {
        return ApiClient.userService.updateUser(request)
    }

    fun logout(): Call<UserDTO.Response> {
        return ApiClient.userService.logout()
    }

    fun getLoanHistory(): Call<LoanDTO.Response> {
        return ApiClient.userService.getLoanHistory()
    }

}