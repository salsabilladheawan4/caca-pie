package com.example.caca_pie

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ValidationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_validation)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tvData = findViewById<TextView>(R.id.tvData)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val btnKembali = findViewById<Button>(R.id.btnKembali)

        val sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE)
        val nama = sharedPref.getString("nama_reg", "")
        val email = sharedPref.getString("email_reg", "")
        val tanggalLahir = sharedPref.getString("tanggal_lahir_reg", "")
        val jenisKelamin = sharedPref.getString("jenis_kelamin_reg", "")
        val username = sharedPref.getString("username_reg", "")
        val password = sharedPref.getString("password_reg", "")
        val confirmPassword = sharedPref.getString("confirm_password_reg", "")

        val dataText = """
            Nama: $nama
            Email: $email
            Tanggal Lahir: $tanggalLahir
            Jenis Kelamin: $jenisKelamin
            Username: $username
            Password: $password
            Confirm Password: $confirmPassword
        """.trimIndent()
        
        tvData.text = dataText

        btnSubmit.setOnClickListener {
            var isValid = true
            
            if (nama.isNullOrEmpty() || email.isNullOrEmpty() || tanggalLahir.isNullOrEmpty() ||
                jenisKelamin.isNullOrEmpty() || username.isNullOrEmpty() || password.isNullOrEmpty() ||
                confirmPassword.isNullOrEmpty()) {
                isValid = false
                Toast.makeText(this, "Semua inputan tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            } else if (password != confirmPassword) {
                isValid = false
                Toast.makeText(this, "Password dan Confirm Password harus sama!", Toast.LENGTH_SHORT).show()
            }

            if (isValid) {
                Toast.makeText(this, "Registrasi Berhasil", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, com.example.caca_pie.pertemuan_6.AuthActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }

        btnKembali.setOnClickListener {
            finish()
        }
    }
}
