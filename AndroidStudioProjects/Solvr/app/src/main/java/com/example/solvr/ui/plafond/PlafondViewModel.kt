package com.example.solvr.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.solvr.models.PlafonDTO
import com.example.solvr.repository.PlafonRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlafondViewModel : ViewModel() {


    private val plafonRepository = PlafonRepository()

    private val _plafonList = MutableLiveData<PlafonDTO.ResponseAllPlafon>()
    val plafonList: LiveData<PlafonDTO.ResponseAllPlafon> get() = _plafonList

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun fetchAllPlafon() {
        plafonRepository.getAllPlafon().enqueue(object : Callback<PlafonDTO.ResponseAllPlafon> {
            override fun onResponse(
                call: Call<PlafonDTO.ResponseAllPlafon>,
                response: Response<PlafonDTO.ResponseAllPlafon>
            ) {
                if (response.isSuccessful && response.body()?.status == 200) {
                    _plafonList.postValue(response.body())
                } else {
                    _errorMessage.postValue("Gagal memuat data plafon: ${response.body()?.message ?: response.message()}")
                }
            }

            override fun onFailure(call: Call<PlafonDTO.ResponseAllPlafon>, t: Throwable) {
                _errorMessage.postValue("Terjadi kesalahan: ${t.message}")
            }
        })
    }
}

