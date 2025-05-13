package com.example.solvr.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.solvr.models.LoanDTO
import com.example.solvr.repository.UserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryViewModel : ViewModel() {
    private val userRepository = UserRepository()

    private val _loanHistory = MutableLiveData<List<LoanDTO.DataItem>>()
    val loanHistory: LiveData<List<LoanDTO.DataItem>> get() = _loanHistory

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun fetchHistory() {
        userRepository.getLoanHistory().enqueue(object : Callback<LoanDTO.Response> {
            override fun onResponse(
                call: Call<LoanDTO.Response>,
                response: Response<LoanDTO.Response>
            ) {
                if (response.isSuccessful) {
                    response.body()?.data?.let {
                        _loanHistory.postValue(it.filterNotNull()) // filter out null items
                    } ?: run {
                        _errorMessage.postValue("Data is empty or null")
                    }
                } else {
                    _errorMessage.postValue("Failed to load loan history: ${response.code()} ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoanDTO.Response>, t: Throwable) {
                _errorMessage.postValue("Network error: ${t.localizedMessage ?: "Unknown error"}")
            }
        })
    }
}
