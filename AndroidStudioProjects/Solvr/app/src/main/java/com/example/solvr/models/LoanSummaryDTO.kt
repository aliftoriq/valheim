package com.example.solvr.models

class LoanSummaryDTO {
    data class Response(
        val data: Data? = null,
        val message: String? = null,
        val status: Int? = null
    )

    data class Data(
        val remainingPlafon: Any? = null,
        val monthlyPayment: Any? = null,
        val name: String? = null,
        val activeLoans: List<ActiveLoansItem?>? = null,
        val accountNumber: String? = null,
        val plafonPackage: PlafonPackage? = null,
        val remainingLoan: Any? = null
    )

    data class PlafonPackage(
        val interestRate: Any? = null,
        val amount: Int? = null,
        val level: Int? = null,
        val name: String? = null,
        val id: Int? = null,
        val maxTenorMonths: Int? = null
    )

    data class UserCustomer(
        val nik: String? = null,
        val address: String? = null,
        val housingStatus: String? = null,
        val phone: String? = null,
        val totalPaidLoan: Any? = null,
        val name: String? = null,
        val motherName: String? = null,
        val id: String? = null,
        val accountNumber: Any? = null,
        val birthDate: String? = null,
        val monthlyIncome: Any? = null
    )

    data class ActiveLoansItem(
        val housingStatus: Any? = null,
        val userEmployee: Any? = null,
        val branchManagerNotes: Any? = null,
        val latitude: Any? = null,
        val reviewedAt: Any? = null,
        val approvedAt: Any? = null,
        val loanAmount: Any? = null,
        val loanTenor: Int? = null,
        val userCustomer: UserCustomer? = null,
        val monthlyPayment: Any? = null,
        val requestedAt: String? = null,
        val backOfficeNotes: Any? = null,
        val marketingNotes: Any? = null,
        val name: Any? = null,
        val id: String? = null,
        val status: String? = null,
        val disbursedAt: Any? = null,
        val longitude: Any? = null
    )


}