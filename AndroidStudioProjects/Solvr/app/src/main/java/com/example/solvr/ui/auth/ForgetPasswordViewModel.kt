package com.example.solvr.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.solvr.models.AuthDTO
import com.example.solvr.models.CommonDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgetPasswordViewModel : ViewModel() {

    private val _forgetPasswordResult = MutableLiveData<Result<CommonDTO.ResponseTemplate>>()
    val forgetPasswordResult: LiveData<Result<CommonDTO.ResponseTemplate>> = _forgetPasswordResult

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun forgotPassword(email: String) {
        val request = AuthDTO.ForgotPasswordRequest(email)
        ApiClient.authService.forgetPassword(request).enqueue(object : Callback<CommonDTO.ResponseTemplate> {
            override fun onResponse(call: Call<CommonDTO.ResponseTemplate>, response: Response<CommonDTO.ResponseTemplate>) {
                if (response.isSuccessful && response.body() != null) {
                    _forgetPasswordResult.value = Result.success(response.body()!!)
                } else {
                    _forgetPasswordResult.value = Result.failure(Throwable("Gagal ganti password: ${response.message()}"))
                }
            }

            override fun onFailure(call: Call<CommonDTO.ResponseTemplate>, t: Throwable) {
                _errorMessage.value = "Error: ${t.message}"
            }
        })
    }
}
