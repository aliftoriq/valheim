package com.example.solvr.models

class AuthDTO {
    data class LoginRequest(
        val username: String,
        val password: String
    )

    data class LoginResponse(
        val status: Int,
        val message: String,
        val data: Data
    )

    data class Data(
        val token: String,
        val features: List<String>,
        val user: User
    )

    data class User(
        val name: String,
        val username: String,
        val role: String,
        val status: String?,
        val deleted: Boolean
    )

    data class RegisterRequest(
        val name: String,
        val username: String,
        val password: String,
        val role: Role
    )

    data class Role(
        val id: Int,
    )

    data class FirebaseTokenRequest(
        val token: String
    )
}
