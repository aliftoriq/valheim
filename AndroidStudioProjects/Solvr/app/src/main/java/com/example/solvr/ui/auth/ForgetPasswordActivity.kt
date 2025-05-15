package com.example.solvr.ui.auth

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.solvr.R
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

import android.widget.Toast

class ForgetPasswordActivity : AppCompatActivity() {

    private lateinit var edtEmail: TextInputEditText
    private lateinit var btnSend: MaterialButton
    private lateinit var viewModel: ForgetPasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)

        // Bind views
        edtEmail = findViewById(R.id.edtEmail)
        btnSend = findViewById(R.id.btnSend)

        // Setup ViewModel
        viewModel = ViewModelProvider(this)[ForgetPasswordViewModel::class.java]

        // Setup listener
        btnSend.setOnClickListener {
            val email = edtEmail.text.toString().trim()
            if (email.isEmpty()) {
                edtEmail.error = "Email tidak boleh kosong"
            } else {
                viewModel.forgotPassword(email)
            }
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.forgetPasswordResult.observe(this) { result ->
            result.onSuccess {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                if (it.status == 200) {
                    finish() // selesai jika berhasil
                }
            }.onFailure {
                Toast.makeText(this, it.message ?: "Terjadi kesalahan", Toast.LENGTH_LONG).show()
            }
        }

        viewModel.errorMessage.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
    }
}

