package com.example.caca_pie

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.caca_pie.pertemuan_5.FifthActivity
import com.example.caca_pie.pertemuan_5.WebViewActivity
import com.example.caca_pie.pertemuan_6.AuthActivity
import com.example.caca_pie.pertemuan_6.DeveloperProfileActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        // 1. Kartu Lihat Menu -> FifthActivity
        val cardLihatMenu = findViewById<CardView>(R.id.cardLihatMenu)
        cardLihatMenu.setOnClickListener {
            startActivity(Intent(this, FifthActivity::class.java))
        }

        // 2. Kartu Kunjungi Website -> WebViewActivity
        val cardWebsite = findViewById<CardView>(R.id.cardWebsite)
        cardWebsite.setOnClickListener {
            startActivity(Intent(this, WebViewActivity::class.java))
        }

        // 3. Kartu Developer -> DeveloperProfileActivity
        val cardDeveloper = findViewById<CardView>(R.id.cardDeveloper)
        cardDeveloper.setOnClickListener {
            startActivity(Intent(this, DeveloperProfileActivity::class.java))
        }

        // 4. Kartu Logout -> Konfirmasi & kembali ke AuthActivity
        val cardLogout = findViewById<CardView>(R.id.cardLogout)
        cardLogout.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Konfirmasi Logout")
                .setMessage("Apakah Anda yakin ingin keluar dari akun?")
                .setPositiveButton("Ya") { _, _ ->
                    val sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE)
                    val editor = sharedPref.edit()
                    editor.clear()
                    editor.apply()

                    val intent = Intent(this, AuthActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                .setNegativeButton("Batal") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }
}