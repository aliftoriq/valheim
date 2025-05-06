package com.example.solvr.models

class CommonDTO {
    data class ResponseTemplate(
        val status: Int,
        val message: String,
        val data: Any
    )
}