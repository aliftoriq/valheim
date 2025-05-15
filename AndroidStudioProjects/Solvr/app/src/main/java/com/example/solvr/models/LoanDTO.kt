package com.example.solvr.models

class LoanDTO {
    data class Request (
        val loanAmount: Double,
        val loanTenor: Int,
        val longitude: Double,
        val latitude: Double
    )

    data class Response(
        val data: List<DataItem?>? = null,
        val message: String? = null,
        val status: Int? = null
    )

    data class ResponseApplyLoan(
        val data: Any? = null,
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

    data class LoanApplicationToEmployeesItem(
        val notes: Any? = null,
        val userEmployee: UserEmployee? = null,
        val id: String? = null
    )

    data class UserEmployee(
        val nip: String? = null,
        val name: String? = null,
        val id: String? = null,
        val department: String? = null,
        val branch: Branch? = null,
        val email: String? = null
    )

    data class UserCustomer(
        val nik: String? = null,
        val address: String? = null,
        val housingStatus: String? = null,
        val phone: String? = null,
        val totalPaidLoan: Any? = null,
        val motherName: String? = null,
        val loanApplications: List<Any?>? = null,
        val id: String? = null,
        val accountNumber: String? = null,
        val plafonPackage: PlafonPackage? = null,
        val birthDate: String? = null,
        val monthlyIncome: Any? = null
    )

    data class Branch(
        val latitude: Any? = null,
        val name: String? = null,
        val id: String? = null,
        val longitude: Any? = null
    )

    data class DataItem(
        val housingStatus: String? = null,
        val latitude: Any? = null,
        val reviewedAt: Any? = null,
        val approvedAt: Any? = null,
        val loanAmount: Any? = null,
        val userCustomer: UserCustomer? = null,
        val loanTenor: Int? = null,
        val loanApplicationToEmployees: List<LoanApplicationToEmployeesItem?>? = null,
        val monthlyPayment: Any? = null,
        val requestedAt: String? = null,
        val id: String? = null,
        val status: String? = null,
        val disbursedAt: Any? = null,
        val longitude: Any? = null
    )
}