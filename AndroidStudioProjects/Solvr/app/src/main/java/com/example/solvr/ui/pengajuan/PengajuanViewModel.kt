package com.example.solvr.ui.pengajuan

import ApiClient
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.solvr.models.LoanSummaryDTO
import com.example.solvr.repository.UserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PengajuanViewModel : ViewModel(){


    private val _loanSummary = MutableLiveData<LoanSummaryDTO.Response>()
    val loanSummary: MutableLiveData<LoanSummaryDTO.Response> = _loanSummary

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun fetchLoanSummary() {
        ApiClient.userService.getLoanSummary().enqueue(object : Callback<LoanSummaryDTO.Response> {
            override fun onResponse(
                call: Call<LoanSummaryDTO.Response>,
                response: Response<LoanSummaryDTO.Response>
            ) {
                if (response.isSuccessful) {
                    _loanSummary.postValue(response.body())
                } else {
                    _errorMessage.postValue("Failed to load loan summary: ${response.code()} ${response.message()}")
                }
            }
            override fun onFailure(call: Call<LoanSummaryDTO.Response>, t: Throwable) {
                _errorMessage.postValue("Network error: ${t.localizedMessage ?: "Unknown error"}")
            }
        })
    }




}