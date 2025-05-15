package com.yourapp.ui.profile

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.solvr.utils.SessionManager
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(application)

    val userName = MutableLiveData<String>()
    val isUserLoggedIn = MutableLiveData<Boolean>()
    val uploadResult = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()

    init {
        checkUserLoginStatus()
    }

    private fun checkUserLoginStatus() {
        val token = sessionManager.getAuthToken()
        isUserLoggedIn.value = !token.isNullOrEmpty()
        if (!token.isNullOrEmpty()) {
            userName.value = sessionManager.getUserName() ?: "Solvr-gengs"
        }
    }

    fun logout() {
        viewModelScope.launch {
            sessionManager.clearSession()
            isUserLoggedIn.value = false
        }
    }

    fun uploadProfileImage(context: Context, imageFile: File) {
        val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", imageFile.name, requestFile)

        ApiClient.userService.uploadProfileImage(body)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        uploadResult.postValue(true)
                    } else {
                        errorMessage.postValue("Gagal upload: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    errorMessage.postValue("Error: ${t.localizedMessage}")
                }
            })
    }
}

