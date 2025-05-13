package com.example.solvr.ui.auth

import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.solvr.models.AuthDTO.LoginRequest
import com.example.solvr.models.AuthDTO.LoginResponse
import com.example.solvr.repository.AuthRepository
import com.example.solvr.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _loginResult = MutableLiveData<LoginResponse?>()
    val loginResult: LiveData<LoginResponse?> = _loginResult

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _fcmTokenResult = MutableLiveData<Boolean>()
    val fcmTokenResult: LiveData<Boolean> = _fcmTokenResult

    fun sendFcmToken(token: String) {
        repository.saveFirebaseToken(token).enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                _fcmTokenResult.postValue(response.isSuccessful)
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                _fcmTokenResult.postValue(false)
            }
        })
    }

    fun loginUser(email: String, password: String) {
        val request = LoginRequest(email, password)

        repository.loginUser(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    _loginResult.postValue(response.body())
                } else {
                    _error.postValue("Login gagal: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _error.postValue("Error: ${t.message}")
            }
        })
    }
}
