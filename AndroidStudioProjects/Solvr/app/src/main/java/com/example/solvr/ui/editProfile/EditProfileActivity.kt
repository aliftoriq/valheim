package com.example.solvr.ui.profile

import android.Manifest
import android.app.DatePickerDialog
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.solvr.R
import com.example.solvr.models.UserDTO
import com.example.solvr.ui.editProfile.EditProfileViewModel
import com.example.solvr.utils.FileUtil
import com.example.solvr.utils.SessionManager
import com.google.android.material.textfield.TextInputLayout
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditProfileActivity : AppCompatActivity() {

    private val viewModel: EditProfileViewModel by viewModels()
    private var isUserExist = false

    private lateinit var etName: EditText
    private lateinit var etNik: EditText
    private lateinit var etAddress: EditText
    private lateinit var etPhone: EditText
    private lateinit var etMotherName: EditText
    private lateinit var etBirthDate: EditText
    private lateinit var etAccountNumber: EditText
    private lateinit var etMonthlyIncome: EditText

    private lateinit var housingStatusLayout: TextInputLayout
    private lateinit var housingStatusDropdown: AutoCompleteTextView

    private lateinit var btnSave: Button
    private lateinit var btnEdit: Button

    private var imageTypeToUpload: String? = null
    private lateinit var ivProfilePicture: ImageView
    private lateinit var ivKtp: ImageView
    private lateinit var ivSelfie: ImageView

    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                ivProfilePicture = findViewById(R.id.ivProfilePicture)
                ivKtp = findViewById(R.id.ivKtp)
                ivSelfie = findViewById(R.id.ivSelfie)

                val file = FileUtil.from(this, uri)
                when (imageTypeToUpload) {
                    "profile" -> {
                        ivProfilePicture.setImageURI(uri)
                        viewModel.uploadProfilePicture(this, file)
                    }

                    "ktp" -> {
                        ivKtp.setImageURI(uri)
                        viewModel.uploadKtp(this, file)
                    }

                    "selfie" -> {
                        ivSelfie.setImageURI(uri)
                        viewModel.uploadSelfie(this, file)
                    }
                }
            }
        }

    private lateinit var cameraImageUri: Uri
    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                when (imageTypeToUpload) {
                    "profile" -> {
                        ivProfilePicture.setImageURI(cameraImageUri)
                        FileUtil.from(this, cameraImageUri)
                            ?.let { viewModel.uploadProfilePicture(this, it) }
                    }

                    "ktp" -> {
                        ivKtp.setImageURI(cameraImageUri)
                        FileUtil.from(this, cameraImageUri)?.let { viewModel.uploadKtp(this, it) }
                    }

                    "selfie" -> {
                        ivSelfie.setImageURI(cameraImageUri)
                        FileUtil.from(this, cameraImageUri)
                            ?.let { viewModel.uploadSelfie(this, it) }
                    }
                }
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val sessionManager: SessionManager by lazy {
            SessionManager(this)
        }

        ivProfilePicture = findViewById(R.id.ivProfilePicture)
        ivKtp = findViewById(R.id.ivKtp)
        ivSelfie = findViewById(R.id.ivSelfie)


        ivProfilePicture.setOnClickListener {
            imageTypeToUpload = "profile"
            requestPermissionsFile()
        }

        ivKtp.setOnClickListener {
            imageTypeToUpload = "ktp"
            requestPermissionsFile()
        }

        ivSelfie.setOnClickListener {
            imageTypeToUpload = "selfie"
            requestPermissionsFile()
        }


        val displayName = findViewById<TextView>(R.id.displayName)

        // Inisialisasi semua input field
        etName = findViewById(R.id.etName)
        etNik = findViewById(R.id.etNik)
        etAddress = findViewById(R.id.etAddress)
        etPhone = findViewById(R.id.etPhone)
        etMotherName = findViewById(R.id.etMotherName)
        etBirthDate = findViewById(R.id.etBirthDate)
        etAccountNumber = findViewById(R.id.etAccountNumber)
        etMonthlyIncome = findViewById(R.id.etMonthlyIncome)

        housingStatusLayout = findViewById(R.id.housingStatusLayout)
        housingStatusDropdown = findViewById(R.id.housingStatusDropdown)

        btnSave = findViewById(R.id.btnSave)
        btnEdit = findViewById(R.id.btnEdit)

        val cardInfoEmpty = findViewById<View>(R.id.tvEmptyInfo)

        // Setup dropdown
        val housingOptions = listOf("Milik Sendiri", "Milik Keluarga", "Menyewa", "Kos")
        val adapter = ArrayAdapter(this, R.layout.item_dropdown, housingOptions)
        housingStatusDropdown.setAdapter(adapter)

        // Setup date picker
        setupDatePicker()

        btnEdit.visibility = View.GONE

        displayName.text = sessionManager.getUserName()

        // Observe user detail
        viewModel.userDetail.observe(this) { user ->
            if (user == null) {
                isUserExist = false
                if (!etName.isEnabled) etName.setText(sessionManager.getUserName())
                cardInfoEmpty.visibility = View.VISIBLE
            } else {
                isUserExist = true
                btnEdit.visibility = View.VISIBLE
                cardInfoEmpty.visibility = View.GONE

                displayName.text = user.name

                if (!etName.isEnabled) etName.setText(user.name)
                if (!etNik.isEnabled) etNik.setText(user.nik)
                if (!etAddress.isEnabled) etAddress.setText(user.address)
                if (!etPhone.isEnabled) etPhone.setText(user.phone)
                if (!etMotherName.isEnabled) etMotherName.setText(user.motherName)
                if (!etBirthDate.isEnabled) etBirthDate.setText(user.birthDate?.substring(0, 10))
                if (!etAccountNumber.isEnabled) etAccountNumber.setText(user.accountNumber)
                if (!etMonthlyIncome.isEnabled) user.monthlyIncome?.let {
                    etMonthlyIncome.setText(it.toString())
                }
                if (!housingStatusDropdown.isEnabled) housingStatusDropdown.setText(
                    user.housingStatus,
                    false
                )

                if (user.urlProfilePicture != null) {
                    loadImageFromUrl(user.urlProfilePicture, ivProfilePicture)
                }
                if (user.urlKtp != null) {
                    loadImageFromUrl(user.urlKtp, ivKtp)
                }
                if (user.urlSelfie != null) {
                    loadImageFromUrl(user.urlSelfie, ivSelfie)
                }

            }
        }

        viewModel.isHaveDetail.observe(this) {
            if (it == true) {
                setFormEnabled(true)
                isUserExist = false
                btnEdit.visibility = View.GONE
                cardInfoEmpty.visibility = View.VISIBLE
                Toast.makeText(this, "Lengkapi data Anda", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.errorMessage.observe(this) { message ->
            if (viewModel.isHaveDetail.value != true) {
                setFormEnabled(true)
                isUserExist = false
                btnEdit.visibility = View.GONE
                cardInfoEmpty.visibility = View.VISIBLE
                Toast.makeText(this, message ?: "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
            }
        }


        viewModel.navigateToLogin.observe(this) { shouldNavigate ->
            if (shouldNavigate == true) {
                Toast.makeText(this, "Session expired, silakan login ulang", Toast.LENGTH_LONG)
                    .show()
                finish()
            }
        }

        viewModel.fetchUserDetail()

        viewModel.updateSuccess.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show()
                viewModel.fetchUserDetail()
                sessionManager.saveUserName(etName.text.toString())
            }
        }



        btnSave.setOnClickListener {
            val monthlyIncomeText = etMonthlyIncome.text.toString()
            val monthlyIncome = if (monthlyIncomeText.isNotEmpty()) {
                try {
                    monthlyIncomeText.toDouble()
                } catch (e: NumberFormatException) {
                    Toast.makeText(this, "Format Pemasukan Bulanan tidak valid", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }
            } else null

            val request = UserDTO.Request(
                name = etName.text.toString(),
                nik = etNik.text.toString(),
                address = etAddress.text.toString(),
                phone = etPhone.text.toString(),
                motherName = etMotherName.text.toString(),
                birthDate = etBirthDate.text.toString(),
                accountNumber = etAccountNumber.text.toString(),
                housingStatus = housingStatusDropdown.text.toString(),
                monthlyIncome = monthlyIncome
            )

            if (isUserExist) {
                viewModel.updateUser(request)
            } else {
                viewModel.createUser(request)
            }

            setFormEnabled(false)

            btnEdit.text = "Edit"
            btnEdit.backgroundTintList = ContextCompat.getColorStateList(this, R.color.primary)
        }

        btnEdit.setOnClickListener {
            val isEditing = etName.isEnabled
            if (isEditing) {
                setFormEnabled(false)
                btnEdit.text = "Edit"
                btnEdit.backgroundTintList = ContextCompat.getColorStateList(this, R.color.primary)
                viewModel.fetchUserDetail()
                Toast.makeText(this, "Perubahan dibatalkan", Toast.LENGTH_SHORT).show()
            } else {
                setFormEnabled(true)
                btnEdit.text = "Cancel"
                btnEdit.backgroundTintList =
                    ContextCompat.getColorStateList(this, R.color.secondary)
                Toast.makeText(this, "Form bisa diubah sekarang", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupDatePicker() {
        val calendar = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            etBirthDate.setText(dateFormat.format(calendar.time))
        }

        etBirthDate.setOnClickListener {
            if (etBirthDate.isEnabled) {
                DatePickerDialog(
                    this,
                    dateSetListener,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        }
    }

    private fun setFormEnabled(enabled: Boolean) {
        etName.isEnabled = enabled
        etNik.isEnabled = enabled
        etAddress.isEnabled = enabled
        etPhone.isEnabled = enabled
        etMotherName.isEnabled = enabled
        etBirthDate.isEnabled = enabled
        etAccountNumber.isEnabled = enabled
        housingStatusDropdown.isEnabled = enabled
        etMonthlyIncome.isEnabled = enabled
        btnSave.isEnabled = enabled
    }

    private fun loadImageFromUrl(url: String, imageView: ImageView) {
        Thread {
            try {
                val inputStream = java.net.URL(url).openStream()
                val bitmap = android.graphics.BitmapFactory.decodeStream(inputStream)
                runOnUiThread {
                    imageView.setImageBitmap(bitmap)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    private fun openCamera() {
        val photoFile = File.createTempFile("IMG_", ".jpg", cacheDir)
        cameraImageUri = FileProvider.getUriForFile(
            this,
            "com.example.solvr.provider",
            photoFile
        )
        cameraLauncher.launch(cameraImageUri)
    }


    private fun showImagePickerOptions() {
        val options = arrayOf("Ambil dari Kamera", "Pilih dari Galeri")
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Pilih Gambar")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> openCamera()
                    1 -> imagePickerLauncher.launch("image/*")
                }
            }.show()
    }

    private fun requestPermissionsFile() {
        val permissions = mutableListOf<String>()

        // Memeriksa versi Android dan menambahkan permission yang sesuai
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Untuk Android 13 (API 33) dan lebih tinggi
            permissions.add(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            // Untuk versi Android sebelum API 33
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        permissions.add(Manifest.permission.CAMERA)


        permissionLauncher.launch(permissions.toTypedArray())
    }

    // Fungsi yang dipanggil saat meminta izin
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val cameraGranted = permissions[Manifest.permission.CAMERA] == true
        val storageGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions[Manifest.permission.READ_MEDIA_IMAGES] == true
        } else {
            permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true
        }

        if (cameraGranted && storageGranted) {
            showImagePickerOptions()

        } else {
            Toast.makeText(this, "Izin kamera dan penyimpanan diperlukan", Toast.LENGTH_SHORT).show()
        }
    }


}
