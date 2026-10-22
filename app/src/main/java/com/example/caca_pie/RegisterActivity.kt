package com.example.caca_pie

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Calendar

class RegisterActivity : AppCompatActivity() {
    
    private var selectedDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val etNama = findViewById<EditText>(R.id.etNama)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val btnTanggalLahir = findViewById<Button>(R.id.btnTanggalLahir)
        val rgJenisKelamin = findViewById<RadioGroup>(R.id.rgJenisKelamin)
        val etUsernameReg = findViewById<EditText>(R.id.etUsernameReg)
        val etPasswordReg = findViewById<EditText>(R.id.etPasswordReg)
        val etConfirmPasswordReg = findViewById<EditText>(R.id.etConfirmPasswordReg)
        val btnSelanjutnya = findViewById<Button>(R.id.btnSelanjutnya)
        val btnBackToLogin = findViewById<android.widget.ImageView>(R.id.btnBackToLogin)

        btnBackToLogin.setOnClickListener {
            finish()
        }

        btnTanggalLahir.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                btnTanggalLahir.text = selectedDate
            }, year, month, day).show()
        }

        btnSelanjutnya.setOnClickListener {
            val nama = etNama.text.toString()
            val email = etEmail.text.toString()
            
            val selectedRbId = rgJenisKelamin.checkedRadioButtonId
            val jenisKelamin = if (selectedRbId != -1) {
                findViewById<RadioButton>(selectedRbId).text.toString()
            } else ""

            val username = etUsernameReg.text.toString()
            val password = etPasswordReg.text.toString()
            val confirmPassword = etConfirmPasswordReg.text.toString()

            val sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString("nama_reg", nama)
            editor.putString("email_reg", email)
            editor.putString("tanggal_lahir_reg", selectedDate)
            editor.putString("jenis_kelamin_reg", jenisKelamin)
            editor.putString("username_reg", username)
            editor.putString("password_reg", password)
            editor.putString("confirm_password_reg", confirmPassword)
            editor.apply()

            val intent = Intent(this, ValidationActivity::class.java)
            startActivity(intent)
        }
    }
}
