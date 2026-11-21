package com.example.caca_pie.pertemuan_6

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.caca_pie.BaseActivity
import com.example.caca_pie.R
import com.example.caca_pie.RegisterActivity
import com.example.caca_pie.data.api.SupabaseApiClient
import kotlinx.coroutines.launch

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_auth)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvSignUp = findViewById<TextView>(R.id.tvSignUp)

        val sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE)

        tvSignUp.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Username dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            btnLogin.isEnabled = false
            btnLogin.text = "Memeriksa..."

            lifecycleScope.launch {
                var authenticated = false
                var displayName = username

                try {
                    val response = SupabaseApiClient.apiService.loginUser(
                        usernameFilter = "eq.$username",
                        passwordFilter = "eq.$password"
                    )
                    if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                        val user = response.body()!!.first()
                        authenticated = true
                        displayName = user.nama.ifBlank { user.username }
                    }
                } catch (_: Exception) {
                    // Fallback ke validasi lokal bila offline
                }

                if (!authenticated) {
                    val spUsername = sharedPref.getString("username_reg", "")
                    val spPassword = sharedPref.getString("password_reg", "")
                    if (username == password || (username == spUsername && password == spPassword)) {
                        authenticated = true
                    }
                }

                btnLogin.isEnabled = true
                btnLogin.text = "Login"

                if (authenticated) {
                    sharedPref.edit()
                        .putBoolean("isLogin", true)
                        .putString("username", username)
                        .putString("nama", displayName)
                        .apply()

                    Toast.makeText(this@AuthActivity, "Login Berhasil", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@AuthActivity, BaseActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                } else {
                    AlertDialog.Builder(this@AuthActivity)
                        .setTitle("Login Gagal")
                        .setMessage("Username atau Password tidak ditemukan. Silakan daftar terlebih dahulu atau periksa kembali akun Anda.")
                        .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                        .show()
                }
            }
        }
    }
}
