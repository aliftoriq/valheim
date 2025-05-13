package com.example.solvr.viewmodel.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.solvr.models.AuthDTO
import com.example.solvr.models.AuthDTO.RegisterRequest
import com.example.solvr.models.CommonDTO.ResponseTemplate
import com.example.solvr.repository.AuthRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {

    private val authRepository = AuthRepository()

    private val _registerSuccess = MutableLiveData<Boolean>()
    val registerSuccess: LiveData<Boolean> = _registerSuccess

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun registerUser(name: String, email: String, password: String) {
        _isLoading.value = true

        val request = RegisterRequest(
            name = name,
            username = email,
            password = password,
            role = AuthDTO.Role(id = 1)
        )

        authRepository.registerUser(request).enqueue(object : Callback<ResponseTemplate> {
            override fun onResponse(call: Call<ResponseTemplate>, response: Response<ResponseTemplate>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _registerSuccess.value = true
                } else {
                    val message = when (response.code()) {
                        400 -> "Bad request"
                        409 -> "Email sudah terdaftar"
                        else -> "Registrasi gagal (Error ${response.code()})"
                    }
                    _errorMessage.value = message
                }
            }

            override fun onFailure(call: Call<ResponseTemplate>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Error: ${t.message}"
            }
        })
    }
}
