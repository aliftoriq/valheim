package com.example.solvr.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.solvr.models.AuthDTO
import com.example.solvr.models.CommonDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordViewModel : ViewModel() {

    private val _changePasswordResult = MutableLiveData<Result<CommonDTO.ResponseTemplate>>()
    val changePasswordResult: LiveData<Result<CommonDTO.ResponseTemplate>> = _changePasswordResult

    fun changePassword(oldPassword: String, newPassword: String) {
        val request = AuthDTO.ChangePasswordRequest(oldPassword, newPassword)

        ApiClient.authService.cangePassword(request).enqueue(object : Callback<CommonDTO.ResponseTemplate> {
            override fun onResponse(
                call: Call<CommonDTO.ResponseTemplate>,
                response: Response<CommonDTO.ResponseTemplate>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    _changePasswordResult.value = Result.success(response.body()!!)
                } else {
                    _changePasswordResult.value = Result.failure(Throwable("Gagal ganti password: ${response.message()}"))
                }
            }

            override fun onFailure(call: Call<CommonDTO.ResponseTemplate>, t: Throwable) {
                _changePasswordResult.value = Result.failure(Throwable("Error: ${t.message}"))
            }
        })
    }
}
