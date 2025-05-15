package com.example.solvr.models

class UserDTO {
    data class Request(
        val name: String? = null,
        val nik: String? = null,
        val address: String? = null,
        val housingStatus: String? = null,
        val phone: String? = null,
        val motherName: String? = null,
        val accountNumber: String? = null,
        val birthDate: String? = null,
        val monthlyIncome: Double? = null
    )

    data class Response(
        val data: Data? = null,
        val message: String? = null,
        val status: Int? = null
    )

    data class PlafonPackage(
        val interestRate: Any? = null,
        val amount: Int? = null,
        val level: Int? = null,
        val name: String? = null,
        val id: Int? = null,
        val maxTenorMonths: Int? = null
    )

    data class Data(
        val nik: String? = null,
        val address: String? = null,
        val housingStatus: String? = null,
        val phone: String? = null,
        val totalPaidLoan: Int? = null,
        val name: String? = null,
        val motherName: String? = null,
        val id: String? = null,
        val accountNumber: String? = null,
        val plafonPackage: PlafonPackage? = null,
        val birthDate: String? = null,
        val monthlyIncome: Any? = null,
        val urlProfilePicture: String? = null,
        val urlKtp: String? = null,
        val urlSelfie: String? = null
    )
}