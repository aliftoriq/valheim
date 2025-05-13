package com.example.solvr.ui.pengajuan

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.solvr.R
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.textfield.TextInputEditText
import java.text.NumberFormat
import java.util.Locale

class PengajuanFragment : Fragment() {

    private lateinit var seekBar: SeekBar
    private lateinit var toggleGroup: MaterialButtonToggleGroup
    private lateinit var edtLoanAmount: TextInputEditText
    private lateinit var txtCicilanBulanan: TextView
    private lateinit var txtTotalPembayaran: TextView
    private lateinit var btnSubmitLoan: Button
    private lateinit var viewModel: PengajuanViewModel

    private lateinit var tvName: TextView
    private lateinit var tvPlafonPackage: TextView
    private lateinit var tvRekening: TextView
    private lateinit var tvActiveLoan: TextView



    private var selectedAmount: Int = 1_000_000
    private var selectedTenor: Int = 6

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pengajuan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val animTop = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_top)
        val animBottom = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_bottom)
        val animLeft = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_out_left)
        val animRight = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_right)
        val animFadeIn = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
        val animFadeOut = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out)

// Terapkan animasi ke masing-masing view
        view.findViewById<FrameLayout>(R.id.headerContainer).startAnimation(animBottom)

        seekBar = view.findViewById(R.id.seekLoanAmount)
        toggleGroup = view.findViewById(R.id.tenorToggleGroup)
        edtLoanAmount = view.findViewById(R.id.edtLoanAmount)
        txtCicilanBulanan = view.findViewById(R.id.txtCicilanBulanan)
        txtTotalPembayaran = view.findViewById(R.id.txtTotalPembayaran)
        btnSubmitLoan = view.findViewById(R.id.btnAjukan)

        viewModel = ViewModelProvider(this)[PengajuanViewModel::class.java]
        tvName = view.findViewById(R.id.etName)
        tvPlafonPackage = view.findViewById(R.id.etPlafonPackage)
        tvRekening = view.findViewById(R.id.etRekening)
        tvActiveLoan = view.findViewById(R.id.ecActiveLoan)



        // Update nominal pinjaman
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) {
                selectedAmount = (progress / 100000) * 100000
                edtLoanAmount.setText(formatRupiah(selectedAmount))
                updateSimulation()
            }

            override fun onStartTrackingTouch(sb: SeekBar?) {}
            override fun onStopTrackingTouch(sb: SeekBar?) {}
        })

        // Pilih tenor
        toggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                selectedTenor = when (checkedId) {
                    R.id.btnTenor3 -> 3
                    R.id.btnTenor6 -> 6
                    R.id.btnTenor9 -> 9
                    R.id.btnTenor12 -> 12
                    else -> 6
                }
                updateSimulation()
            }
        }

        // Ajukan pinjaman
        btnSubmitLoan.setOnClickListener {
            Toast.makeText(requireContext(), "Pengajuan Rp${formatRupiah(selectedAmount)} selama $selectedTenor bulan berhasil diajukan!", Toast.LENGTH_LONG).show()
            // Tambahkan navigasi atau logika pengajuan di sini
        }

        updateSimulation()

        viewModel.loanSummary.observe(viewLifecycleOwner) { summary ->
            summary?.let {
                tvName.text = "Halo, ${it.data?.name ?: "User"}!"
                tvPlafonPackage.text = it.data?.plafonPackage?.name ?: "-"
                tvRekening.text = "Rekening ${it.data?.accountNumber ?: "-"}"
                val activeLoan = (it.data?.remainingLoan ?: 0).toString().toDoubleOrNull() ?: 0.0
                tvActiveLoan.text = formatRupiah(activeLoan.toInt())

            }
        }

        viewModel.fetchLoanSummary()

        viewModel.errorMessage.observe(viewLifecycleOwner) { msg ->
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        }

    }

    @SuppressLint("SetTextI18n")
    private fun updateSimulation() {
        val interestRate = 0.12 // 12% flat interest
        val totalInterest = selectedAmount * interestRate
        val totalPayment = selectedAmount + totalInterest
        val monthlyInstallment = totalPayment / selectedTenor

        txtCicilanBulanan.text = "Cicilan per bulan: ${formatRupiah(monthlyInstallment.toInt())}"
        txtTotalPembayaran.text = "Total pembayaran: ${formatRupiah(totalPayment.toInt())}"
    }

    private fun formatRupiah(number: Int): String {
        val localeID = Locale("in", "ID")
        val format = NumberFormat.getCurrencyInstance(localeID)
        return format.format(number).replace(",00", "")
    }
}
