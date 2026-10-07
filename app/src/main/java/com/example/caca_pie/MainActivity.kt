package com.example.caca_pie

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.caca_pie.pertemuan_5.FifthActivity
import com.example.caca_pie.pertemuan_5.WebViewActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left + 20.dp, systemBars.top + 20.dp, systemBars.right + 20.dp, systemBars.bottom + 20.dp)
            insets
        }

        val btnMyProject = findViewById<Button>(R.id.btnMyProject)
        btnMyProject.setOnClickListener {
            startActivity(Intent(this, FifthActivity::class.java))
        }

        val btnWebView = findViewById<Button>(R.id.btnWebView)
        btnWebView.setOnClickListener {
            startActivity(Intent(this, WebViewActivity::class.java))
        }
    }

    private val Int.dp: Int
        get() = (this * resources.displayMetrics.density).toInt()
}