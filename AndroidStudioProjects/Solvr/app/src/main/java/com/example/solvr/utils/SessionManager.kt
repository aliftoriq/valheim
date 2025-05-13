package com.example.solvr.utils

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import androidx.core.content.edit


class SessionManager(context: Context) {

    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private val prefs = EncryptedSharedPreferences.create(
        "secure_auth_prefs",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    companion object {
        private const val KEY_AUTH_TOKEN = "auth_token"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_EMAIL = "user_email"
    }

    fun saveAuthToken(token: String) {
        prefs.edit() { putString(KEY_AUTH_TOKEN, token) }
    }

    fun getAuthToken(): String? = prefs.getString(KEY_AUTH_TOKEN, null)

    fun saveUserName(name: String) {
        prefs.edit() { putString(KEY_USER_NAME, name) }
    }

    fun getUserName(): String? = prefs.getString(KEY_USER_NAME, null)

    fun saveUserEmail(email: String) {
        prefs.edit() { putString(KEY_USER_EMAIL, email) }
    }

    fun getUserEmail(): String? = prefs.getString(KEY_USER_EMAIL, null)

    fun clearSession() {
        prefs.edit() { clear() }
    }
}
