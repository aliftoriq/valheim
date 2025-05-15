package com.example.solvr.ui.editProfile

import android.content.Context
import android.content.Intent
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.solvr.models.UserDTO
import com.example.solvr.repository.UserRepository
import com.example.solvr.ui.profile.EditProfileActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class EditProfileViewModel : ViewModel() {

    private val userRepository = UserRepository()

    private val _userDetail = MutableLiveData<UserDTO.Data?>()
    val userDetail: LiveData<UserDTO.Data?> get() = _userDetail

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _navigateToLogin = MutableLiveData<Boolean>()
    val navigateToLogin: LiveData<Boolean> get() = _navigateToLogin

    private val _updateSuccess = MutableLiveData<Boolean>()
    val updateSuccess: LiveData<Boolean> get() = _updateSuccess

    private val _isHaveDetail = MutableLiveData<Boolean>()
    val isHaveDetail: LiveData<Boolean> get() = _isHaveDetail

    private val _uploadResult = MutableLiveData<Boolean>()
    val uploadResult: LiveData<Boolean> get() = _uploadResult

    fun fetchUserDetail() {
        userRepository.getUSerDetail().enqueue(object : Callback<UserDTO.Response> {
            override fun onResponse(
                call: Call<UserDTO.Response>, response: Response<UserDTO.Response>
            ) {
                val code = response.code()
                val body = response.body()

                if (response.isSuccessful && body != null) {
                    _userDetail.postValue(body.data)
                } else if (code == 401) {
                    _errorMessage.postValue("Unauthorized")
                    _navigateToLogin.postValue(true)
                } else if (code == 404) {
                    _isHaveDetail.postValue(true)
                    _errorMessage.postValue("Lengkapi Data")
                } else {
                    _errorMessage.postValue("Gagal memuat data pengguna.")
                }
            }

            override fun onFailure(call: Call<UserDTO.Response>, t: Throwable) {
                _errorMessage.postValue("Kesalahan jaringan: ${t.message}")
            }
        })
    }

    fun createUser(request: UserDTO.Request) {
        userRepository.createUser(request).enqueue(object : Callback<UserDTO.Response> {
            override fun onResponse(
                call: Call<UserDTO.Response>, response: Response<UserDTO.Response>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    _userDetail.postValue(response.body()?.data)
                    _updateSuccess.postValue(true)
                } else {
                    _errorMessage.postValue("Gagal membuat user.")
                }
            }

            override fun onFailure(call: Call<UserDTO.Response>, t: Throwable) {
                _errorMessage.postValue("Kesalahan jaringan saat membuat user: ${t.message}")
            }
        })
    }

    fun updateUser(request: UserDTO.Request) {
        userRepository.updateUser(request).enqueue(object : Callback<UserDTO.Response> {
            override fun onResponse(
                call: Call<UserDTO.Response>, response: Response<UserDTO.Response>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    _userDetail.postValue(response.body()?.data)
                    _updateSuccess.postValue(true)
                } else {
                    _errorMessage.postValue("Gagal memperbarui data.")
                }
            }

            override fun onFailure(call: Call<UserDTO.Response>, t: Throwable) {
                _errorMessage.postValue("Kesalahan jaringan saat update: ${t.message}")
            }
        })
    }

    fun uploadProfilePicture(context: Context, imageFile: File) {
        val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", imageFile.name, requestFile)

        ApiClient.userService.uploadProfileImage(body).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    _uploadResult.postValue(true)
                } else {
                    _errorMessage.postValue("Gagal upload: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                _errorMessage.postValue("Error: ${t.localizedMessage}")
            }
        })
    }

    fun uploadKtp(context: Context, imageFile: File) {
        val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", imageFile.name, requestFile)

        ApiClient.userService.uploadKtpImage(body).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        _uploadResult.postValue(true)
                    } else {
                        _errorMessage.postValue("Gagal upload: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    _errorMessage.postValue("Error: ${t.localizedMessage}")
                }
            })
    }

    fun uploadSelfie(context: Context, imageFile: File) {
        val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", imageFile.name, requestFile)

        ApiClient.userService.uploadSelfieImage(body).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        _uploadResult.postValue(true)
                    } else {
                        _errorMessage.postValue("Gagal upload: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    _errorMessage.postValue("Error: ${t.localizedMessage}")
                }
            })
    }
}
