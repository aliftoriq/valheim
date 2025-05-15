package com.example.solvr.network

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.example.solvr.R

class FirebaseAuthService(private val activity: Activity) {

    private val auth = FirebaseAuth.getInstance()

    private val googleSignInClient: GoogleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        GoogleSignIn.getClient(activity, gso)
    }

    fun getSignInIntent(): Intent {
        return googleSignInClient.signInIntent
    }

    fun signInWithGoogle(idToken: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure(task.exception?.message ?: "Autentikasi gagal")
                }
            }
    }

    fun signOut(onComplete: () -> Unit) {
        auth.signOut()
        googleSignInClient.signOut().addOnCompleteListener { onComplete() }
    }

    fun getCurrentUser() = auth.currentUser
}
