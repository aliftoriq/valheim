package com.example.solvr.ui.auth

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.solvr.R
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.textfield.TextInputEditText

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var viewModel: ChangePasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        val animBottom = AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom)

        viewModel = ViewModelProvider(this)[ChangePasswordViewModel::class.java]

        val shimmerLayout = findViewById<ShimmerFrameLayout>(R.id.shimmerLayout)

        val edtCurrentPassword = findViewById<TextInputEditText>(R.id.edtCurrentPassword)
        val edtNewPassword = findViewById<TextInputEditText>(R.id.edtNewPassword)
        val edtConfirmPassword = findViewById<TextInputEditText>(R.id.edtConfirmPassword)
        val btnChange = findViewById<Button>(R.id.btnChangePassword)
        val headerContainer = findViewById<FrameLayout>(R.id.headerContainer)

        headerContainer.startAnimation(animBottom)

        btnChange.setOnClickListener {
            val oldPass = edtCurrentPassword.text.toString()
            val newPass = edtNewPassword.text.toString()
            val confirmPass = edtConfirmPassword.text.toString()

            if (newPass != confirmPass) {
                Toast.makeText(this, "Password baru tidak cocok", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.changePassword(oldPass, newPass)

            shimmerLayout.visibility = View.VISIBLE
            shimmerLayout.startShimmer()
            shimmerLayout.startAnimation(animBottom)
            btnChange.visibility = View.GONE
        }

        shimmerLayout.visibility = View.GONE

        viewModel.changePasswordResult.observe(this) { result ->

            Handler(Looper.getMainLooper()).postDelayed({
                shimmerLayout.stopShimmer()
                shimmerLayout.visibility = View.GONE
                btnChange.visibility = View.VISIBLE
                btnChange.startAnimation(animBottom)

            }, 1000)
            result.onSuccess {

                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                finish() // Atau navigasi lain
            }.onFailure {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }
        }


    }
}
