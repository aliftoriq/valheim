package com.example.solvr.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.solvr.MainActivity
import com.example.solvr.R
import com.example.solvr.network.FirebaseAuthService
import com.example.solvr.utils.SessionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging

class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()

    private lateinit var firebaseAuthService: FirebaseAuthService
    private val RC_SIGN_IN = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        firebaseAuthService = FirebaseAuthService(this)

        val btnGoogleSignIn = findViewById<ImageView>(R.id.btnGoogleSignIn)
        btnGoogleSignIn.setOnClickListener {
            val signInIntent = firebaseAuthService.getSignInIntent()
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val edtEmail = findViewById<EditText>(R.id.edtEmail)
        val edtPassword = findViewById<EditText>(R.id.edtPassword)
        val btnRegisterPage = findViewById<TextView>(R.id.btnRegisterPage)
        val btnForgetPassword = findViewById<TextView>(R.id.btnForgetPassword)

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

        btnForgetPassword.setOnClickListener {
            startActivity(Intent(this, ForgetPasswordActivity::class.java))
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == RC_SIGN_IN) {
            val task = com.google.android.gms.auth.api.signin.GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(com.google.android.gms.common.api.ApiException::class.java)
                val idToken = account.idToken

                if (idToken != null) {
//                    android.util.Log.d("GoogleIDToken", "ID Token: $idToken")
                    firebaseAuthService.signInWithGoogle(idToken, {
                        // Sukses login Google, simpan nama dan email user
                        val sessionManager = SessionManager(this)
                        sessionManager.saveUserName(account.displayName ?: "")
                        sessionManager.saveUserEmail(account.email ?: "")
                        sessionManager.saveAuthToken(account.idToken ?: "")

                        // Arahkan ke MainActivity
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }, { errorMsg ->
                        Toast.makeText(this, "Login Google gagal: $errorMsg", Toast.LENGTH_SHORT).show()
                    })
                } else {
                    Toast.makeText(this, "ID Token Google kosong", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                Toast.makeText(this, "Login Google error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }

            FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseIdToken = task.result?.token
                    android.util.Log.d("GoogleIDToken", "ID Token: $firebaseIdToken")
                }
            }
        }
    }

}
