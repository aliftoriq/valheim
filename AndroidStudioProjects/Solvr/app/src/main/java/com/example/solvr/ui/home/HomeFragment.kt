package com.example.solvr.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.solvr.R
import com.example.solvr.utils.SessionManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.textfield.TextInputEditText
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale

class HomeFragment : Fragment() {

    private var selectedTenor = 0
    private var selectedAmount = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val animTop = AnimationUtils.loadAnimation(inflater.context, R.anim.slide_in_top)
        val animBottom = AnimationUtils.loadAnimation(inflater.context, R.anim.slide_in_bottom)
        val animLeft = AnimationUtils.loadAnimation(inflater.context, R.anim.slide_out_left)
        val animRight = AnimationUtils.loadAnimation(inflater.context, R.anim.slide_in_right)
        val animFadeIn = AnimationUtils.loadAnimation(inflater.context, R.anim.fade_in)
        val animFadeOut = AnimationUtils.loadAnimation(inflater.context, R.anim.fade_out)

// Terapkan animasi ke masing-masing view
//        view.findViewById<TextView>(R.id.greetingText).startAnimation(animLeft)
        view.findViewById<RelativeLayout>(R.id.cardContainer).startAnimation(animFadeIn)
        view.findViewById<FrameLayout>(R.id.headerContainer).startAnimation(animBottom)
//        view.findViewById<SeekBar>(R.id.seekLoanAmount).startAnimation(animLeft)
//        view.findViewById<MaterialButtonToggleGroup>(R.id.tenorToggleGroup).startAnimation(animBottom)



        val sessionManager = SessionManager(requireContext())
        val greetingText: TextView = view.findViewById(R.id.greetingText)
        val userName = sessionManager.getUserName()

        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        val greeting = when (hour) {
            in 4..10 -> "Selamat Pagi"
            in 11..14 -> "Selamat Siang"
            in 15..17 -> "Selamat Sore"
            else -> "Selamat Malam"
        }

        greetingText.text = if (userName.isNullOrEmpty()) {
            "$greeting,\nSolvr-gengs!"
        } else {
            "$greeting,\n$userName!"
        }

        // Set up SeekBar for Loan Amount
        val seekLoanAmount = view.findViewById<SeekBar>(R.id.seekLoanAmount)
        val edtLoanAmount = view.findViewById<TextInputEditText>(R.id.edtLoanAmount)

        seekLoanAmount.max = 100  // Maksimal 10 juta (100 * 100k)
        seekLoanAmount.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                selectedAmount = progress * 100_000
                edtLoanAmount.setText(selectedAmount.formatRupiah())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Set up Tenor Toggle Button Group
        val tenorToggleGroup = view.findViewById<MaterialButtonToggleGroup>(R.id.tenorToggleGroup)
        val allTenorButtons = listOf(
            view.findViewById<MaterialButton>(R.id.btnTenor3),
            view.findViewById<MaterialButton>(R.id.btnTenor6),
            view.findViewById<MaterialButton>(R.id.btnTenor9),
            view.findViewById<MaterialButton>(R.id.btnTenor12)
        )

        tenorToggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            // Make sure only one button is selected
            if (isChecked) {
                allTenorButtons.forEach { button ->
                    if (button.id == checkedId) {
                        // Change selected button to primary color
                        button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.secondary))
                        button.setTextColor(Color.WHITE)
                    } else {
                        // Reset unselected buttons to secondary color
                        button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primary))
                        button.setTextColor(Color.WHITE )
                    }
                }

                // Set selected tenor value
                selectedTenor = when (checkedId) {
                    R.id.btnTenor3 -> 3
                    R.id.btnTenor6 -> 6
                    R.id.btnTenor9 -> 9
                    R.id.btnTenor12 -> 12
                    else -> 0
                }
            }
        }


        // Set up Calculate Button
        val btnSimulasi = view.findViewById<Button>(R.id.btnCalculate)
        btnSimulasi.setOnClickListener {
            if (selectedAmount == 0 || selectedTenor == 0) {
                Toast.makeText(requireContext(), "Pilih nominal dan tenor terlebih dahulu.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val bungaPerBulan = 0.014
            val biayaAdmin = 50_000.0

            val totalBunga = selectedAmount * bungaPerBulan * selectedTenor
            val totalPembayaran = selectedAmount + totalBunga + biayaAdmin
            val cicilanBulanan = totalPembayaran / selectedTenor

            val simulationContainer = view.findViewById<LinearLayout>(R.id.simulationResultContainer)
            val txtCicilan = view.findViewById<TextView>(R.id.txtCicilanBulanan)
            val txtTotal = view.findViewById<TextView>(R.id.txtTotalPembayaran)
            val txtBunga = view.findViewById<TextView>(R.id.txtBiayaBunga)
            val txtAdmin = view.findViewById<TextView>(R.id.txtBiayaAdmin)

            txtCicilan.text = "Rp ${cicilanBulanan.toInt().formatRupiah()}"
            txtTotal.text = "Rp ${totalPembayaran.toInt().formatRupiah()}"
            txtBunga.text = "Rp ${totalBunga.toInt().formatRupiah()}"
            txtAdmin.text = "Rp ${biayaAdmin.toInt().formatRupiah()}"

            simulationContainer.visibility = View.VISIBLE
        }

        return view
    }

    fun Int.formatRupiah(): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        return formatter.format(this).replace(",00", "")
    }


}

