package com.example.solvr.models

class PlafonDTO {
    data class DataItem(
        val interestRate: Any? = null,
        val amount: Int? = null,
        val level: Int? = null,
        val name: String? = null,
        val id: Int? = null,
        val maxTenorMonths: Int? = null
    )

    data class ResponseAllPlafon(
        val data: List<DataItem?>? = null,
        val message: String? = null,
        val status: Int? = null
    )
}