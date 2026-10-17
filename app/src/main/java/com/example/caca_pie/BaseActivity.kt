package com.example.caca_pie

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.caca_pie.databinding.ActivityBaseBinding
import com.example.caca_pie.pertemuan_7.CartFragment
import com.example.caca_pie.pertemuan_7.FavoriteFragment
import com.example.caca_pie.pertemuan_7.HomeFragment
import com.example.caca_pie.pertemuan_7.ProfileFragment

class BaseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Atur padding agar konten tidak tertutup system bar
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        // Tampilkan HomeFragment saat pertama kali dibuka
        replaceFragment(HomeFragment())

        // Listener untuk setiap item BottomNavigation
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home     -> replaceFragment(HomeFragment())
                R.id.nav_cart     -> replaceFragment(CartFragment())
                R.id.nav_favorite -> replaceFragment(FavoriteFragment())
                R.id.nav_profile  -> replaceFragment(ProfileFragment())
            }
            true
        }
    }

    // Fungsi untuk mengganti fragment yang tampil
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
