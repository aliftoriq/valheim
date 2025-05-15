package com.example.solvr.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.solvr.databinding.ActivityRegisterBinding
import com.example.solvr.models.AuthDTO
import com.example.solvr.models.AuthDTO.RegisterRequest
import com.example.solvr.models.CommonDTO.ResponseTemplate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnLoginPage.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.btnRegister.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val name = binding.edtName.text.toString().trim()
        val email = binding.edtEmail.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()
        val confirmPassword = binding.edtConfirmPassword.text.toString().trim()

        if (!validateInputs(name, email, password, confirmPassword)) {
            return
        }

        val registerRequest = RegisterRequest(
            name = name,
            username = email,
            password = password,
            role = AuthDTO.Role(id = 1)
        )

        showLoading(true)

        ApiClient.authService.register(registerRequest).enqueue(object : Callback<ResponseTemplate> {
            override fun onResponse(
                call: Call<ResponseTemplate>,
                response: Response<ResponseTemplate>
            ) {
                showLoading(false)

                if (response.isSuccessful) {
                    handleRegistrationSuccess(response.body())
                } else {
                    handleRegistrationError(response.code())
                }
            }

            override fun onFailure(call: Call<ResponseTemplate>, t: Throwable) {
                showLoading(false)
                Toast.makeText(
                    this@RegisterActivity,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun validateInputs(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        if (name.isEmpty()) {
            binding.edtName.error = "Nama lengkap harus diisi"
            return false
        }

        if (email.isEmpty()) {
            binding.edtEmail.error = "Email harus diisi"
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edtEmail.error = "Format email tidak valid"
            return false
        }

        if (password.isEmpty()) {
            binding.edtPassword.error = "Password harus diisi"
            return false
        }

        if (password.length < 6) {
            binding.edtPassword.error = "Password minimal 6 karakter"
            return false
        }

        if (password != confirmPassword) {
            binding.edtConfirmPassword.error = "Password tidak sama"
            return false
        }

        return true
    }

    private fun handleRegistrationSuccess(response: ResponseTemplate?) {
        Toast.makeText(
            this,
            "Registrasi berhasil! Silakan login",
            Toast.LENGTH_SHORT
        ).show()

        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun handleRegistrationError(errorCode: Int) {
        val errorMessage = when (errorCode) {
            400 -> "Bad request"
            409 -> "Email sudah terdaftar"
            else -> "Registrasi gagal (Error $errorCode)"
        }

        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.btnRegister.isEnabled = !isLoading
        // You can add a progress bar here if needed
    }
}