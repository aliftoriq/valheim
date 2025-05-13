package com.yourapp.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.solvr.R
import com.example.solvr.ui.auth.LoginActivity
import com.example.solvr.ui.history.HistoryActivity
import com.example.solvr.ui.profile.EditProfileActivity
import com.example.solvr.utils.SessionManager

class ProfileFragment : Fragment() {

    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val nameTextView = view.findViewById<TextView>(R.id.profile_name)

        val btnEditProfile = view.findViewById<TextView>(R.id.btnEditProfile)
        val btnHistory = view.findViewById<TextView>(R.id.btnHistory)
        val btnLogout = view.findViewById<TextView>(R.id.btnLogout)

        profileViewModel.isUserLoggedIn.observe(viewLifecycleOwner, Observer { isLoggedIn ->
            if (!isLoggedIn) {
                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        })

        profileViewModel.userName.observe(viewLifecycleOwner, Observer { userName ->
            nameTextView.text = userName
        })

        btnLogout.setOnClickListener {
            profileViewModel.logout()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        btnEditProfile.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
        }

        btnHistory.setOnClickListener {
            val intent = Intent(requireContext(), HistoryActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}

