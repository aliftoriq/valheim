package com.yourapp.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.solvr.utils.SessionManager
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(application)

    val userName = MutableLiveData<String>()
    val isUserLoggedIn = MutableLiveData<Boolean>()

    init {
        checkUserLoginStatus()
    }

    private fun checkUserLoginStatus() {
        val token = sessionManager.getAuthToken()
        if (token.isNullOrEmpty()) {
            isUserLoggedIn.value = false
        } else {
            isUserLoggedIn.value = true
            userName.value = sessionManager.getUserName() ?: "Solvr-gengs"
        }
    }

    fun logout() {
        viewModelScope.launch {
            sessionManager.clearSession()
            isUserLoggedIn.value = false
        }
    }
}
