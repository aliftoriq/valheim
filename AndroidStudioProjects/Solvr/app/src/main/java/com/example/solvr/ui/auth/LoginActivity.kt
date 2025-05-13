package com.example.solvr.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.solvr.MainActivity
import com.example.solvr.R
import com.example.solvr.utils.SessionManager
import com.google.firebase.messaging.FirebaseMessaging

class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val edtEmail = findViewById<EditText>(R.id.edtEmail)
        val edtPassword = findViewById<EditText>(R.id.edtPassword)
        val btnRegisterPage = findViewById<TextView>(R.id.btnRegisterPage)

        // Observers
        viewModel.loginResult.observe(this) { response ->
            if (response != null) {
                val sessionManager = SessionManager(this)
                sessionManager.saveAuthToken(response.data?.token ?: "")
                sessionManager.saveUserName(response.data?.user?.name ?: "")
                sessionManager.saveUserEmail(response.data?.user?.username ?: "")

                Toast.makeText(
                    this,
                    "Login sukses: ${response.data?.user?.name}",
                    Toast.LENGTH_SHORT
                ).show()

                FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val token = task.result
                        viewModel.sendFcmToken(token)
                    } else {
                        Toast.makeText(this, "Gagal ambil FCM token", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        viewModel.fcmTokenResult.observe(this) { success ->
            if (success) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Gagal simpan token FCM", Toast.LENGTH_SHORT).show()
            }
        }


        viewModel.error.observe(this, Observer { errorMsg ->
            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show()
        })

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

            viewModel.loginUser(email, password)

        }
    }

}
