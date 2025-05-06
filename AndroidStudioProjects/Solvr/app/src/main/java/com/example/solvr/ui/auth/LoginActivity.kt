package com.example.solvr.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.solvr.MainActivity
import com.example.solvr.R
import com.example.solvr.models.AuthDTO.LoginRequest
import com.example.solvr.models.AuthDTO.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val edtEmail = findViewById<EditText>(R.id.edtEmail)
        val edtPassword = findViewById<EditText>(R.id.edtPassword)
        val btnRegisterPage = findViewById<TextView>(R.id.btnRegisterPage)

        btnRegisterPage.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }


        btnLogin.setOnClickListener {
            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan password harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Format email tidak valid", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = LoginRequest(email, password)

            // Show loading indicator here if you have one

            ApiClient.instance.login(request).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {

                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        Toast.makeText(
                            this@LoginActivity,
                            "Login sukses: ${loginResponse?.data?.user?.name}",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Save token if needed
                        // navigate to main activity
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Login gagal: ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    // Hide loading indicator here
                    Toast.makeText(
                        this@LoginActivity,
                        "Error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }

        // Add click listeners for register and google sign-in if needed
        // findViewById<TextView>(R.id.btnRegister).setOnClickListener { ... }
        // findViewById<ImageView>(R.id.btnGoogle).setOnClickListener { ... }
    }
}