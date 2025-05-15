package com.yourapp.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.solvr.R
import com.example.solvr.ui.auth.LoginActivity
import com.example.solvr.ui.history.HistoryActivity
import com.example.solvr.ui.profile.EditProfileActivity
import android.app.Activity
import android.widget.ImageView
import android.widget.Toast
import com.example.solvr.ui.auth.ChangePasswordActivity
import com.example.solvr.utils.FileUtil

class ProfileFragment : Fragment() {

    private val profileViewModel: ProfileViewModel by viewModels()
    private lateinit var imageView: ImageView
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val nameTextView = view.findViewById<TextView>(R.id.profile_name)
        val btnEditProfile = view.findViewById<TextView>(R.id.btnEditProfile)
        val btnHistory = view.findViewById<TextView>(R.id.btnHistory)
        val btnLogout = view.findViewById<TextView>(R.id.btnLogout)
        val btnChangePassword = view.findViewById<TextView>(R.id.btnChangePassword)

        profileViewModel.isUserLoggedIn.observe(viewLifecycleOwner) { isLoggedIn ->
            if (!isLoggedIn) {
                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }

        profileViewModel.userName.observe(viewLifecycleOwner) { userName ->
            nameTextView.text = userName
        }

        profileViewModel.uploadResult.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "Upload berhasil", Toast.LENGTH_SHORT).show()
            }
        }

        profileViewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }

        btnLogout.setOnClickListener {
            profileViewModel.logout()
        }

        btnEditProfile.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }

        btnHistory.setOnClickListener {
            startActivity(Intent(requireContext(), HistoryActivity::class.java))
        }

        btnChangePassword.setOnClickListener {
            startActivity(Intent(requireContext(), ChangePasswordActivity::class.java))
        }

        imageView = view.findViewById(R.id.profile_image)
        imageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val imageUri = data.data!!
            imageView.setImageURI(imageUri) // Tampilkan di ImageView

            val file = FileUtil.from(requireContext(), imageUri)
            profileViewModel.uploadProfileImage(requireContext(), file)
        }
    }
}


