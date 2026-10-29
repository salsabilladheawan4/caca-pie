package com.example.caca_pie.pertemuan_5

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.caca_pie.R
import com.example.caca_pie.databinding.ActivityFifthBinding

class FifthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFifthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFifthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Bar bawah menghindari navigation bar
        val bottomPadding = binding.bottomBar.paddingBottom
        ViewCompat.setOnApplyWindowInsetsListener(binding.bottomBar) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(v.paddingLeft, v.paddingTop, v.paddingRight, bottomPadding + bars.bottom)
            insets
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = getString(R.string.title_details)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        }

        // Listener untuk tombol Add to Cart bawaan (Fitur 2: Keranjang Belanja Dinamis)
        binding.btnAddToCart.setOnClickListener {
            val prefs = getSharedPreferences("user_pref", MODE_PRIVATE)
            val currentQty = prefs.getInt("cart_cappuccino_qty", 0)
            val newQty = if (currentQty > 0) currentQty + 1 else 1

            prefs.edit()
                .putBoolean("cart_has_cappuccino", true)
                .putInt("cart_cappuccino_qty", newQty)
                .apply()

            Toast.makeText(this, "Velvet Cappuccino ($newQty) dimasukkan ke keranjang! 🛒",
                Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        // Cek status favorit dari SharedPreferences dan sesuaikan icon
        val prefs = getSharedPreferences("user_pref", MODE_PRIVATE)
        val isFav = prefs.getBoolean("fav_cappuccino", false)
        val favItem = menu.findItem(R.id.action_favorite)
        if (isFav) {
            favItem?.setIcon(R.drawable.ic_favorite_filled)
        } else {
            favItem?.setIcon(R.drawable.ic_favorite_border)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }

            R.id.action_favorite -> {
                val prefs = getSharedPreferences("user_pref", MODE_PRIVATE)
                val isFav = prefs.getBoolean("fav_cappuccino", false)
                val newFav = !isFav

                // Simpan status baru secara persisten
                prefs.edit().putBoolean("fav_cappuccino", newFav).apply()

                if (newFav) {
                    item.setIcon(R.drawable.ic_favorite_filled)
                    Toast.makeText(this, "Ditambahkan ke menu Favorit ❤️", Toast.LENGTH_SHORT).show()
                } else {
                    item.setIcon(R.drawable.ic_favorite_border)
                    Toast.makeText(this, "Dihapus dari menu Favorit", Toast.LENGTH_SHORT).show()
                }
                true
            }

            R.id.action_search -> {
                Toast.makeText(this, R.string.toast_search, Toast.LENGTH_SHORT).show()
                true
            }

            R.id.action_settings -> {
                Toast.makeText(this, R.string.toast_settings, Toast.LENGTH_SHORT).show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
