package com.example.caca_pie.pertemuan_5

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.caca_pie.R
import com.example.caca_pie.databinding.ActivityFifthBinding
import java.util.Locale

class FifthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFifthBinding
    private var quantity = 1
    private var basePrice = 20.99
    private var selectedSize = "Medium"
    private var selectedSugar = "Normal"

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

        setupSizeSelector()
        setupSugarSelector()
        setupQuantityStepper()
        updatePriceDisplay()

        // Listener untuk tombol Add to Cart bawaan (Fitur 2: Keranjang Belanja Dinamis)
        binding.btnAddToCart.setOnClickListener {
            val prefs = getSharedPreferences("user_pref", MODE_PRIVATE)
            val currentQty = prefs.getInt("cart_cappuccino_qty", 0)
            val newQty = currentQty + quantity

            prefs.edit()
                .putBoolean("cart_has_cappuccino", true)
                .putInt("cart_cappuccino_qty", newQty)
                .apply()

            val formattedTotal = String.format(Locale.US, "$%.2f", basePrice * quantity)
            Toast.makeText(
                this,
                "Velvet Cappuccino ($selectedSize, $selectedSugar) x$quantity dimasukkan ke keranjang ($formattedTotal)",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setupSizeSelector() {
        val sizeChips = listOf(
            binding.chipSizeSmall to ("Small" to 18.99),
            binding.chipSizeMedium to ("Medium" to 20.99),
            binding.chipSizeLarge to ("Large" to 22.99)
        )

        for ((chip, data) in sizeChips) {
            chip.setOnClickListener {
                selectedSize = data.first
                basePrice = data.second
                updateChipStyles(sizeChips.map { it.first }, chip)
                updatePriceDisplay()
            }
        }
    }

    private fun setupSugarSelector() {
        val sugarChips = listOf(
            binding.chipSugarNormal to "Normal",
            binding.chipSugarLess to "Less Sugar",
            binding.chipSugarNo to "No Sugar"
        )

        for ((chip, level) in sugarChips) {
            chip.setOnClickListener {
                selectedSugar = level
                updateChipStyles(sugarChips.map { it.first }, chip)
            }
        }
    }

    private fun updateChipStyles(chips: List<TextView>, selected: TextView) {
        for (chip in chips) {
            if (chip == selected) {
                chip.setBackgroundResource(R.drawable.bg_chip_selected)
                chip.setTextColor(Color.WHITE)
                chip.setTypeface(null, Typeface.BOLD)
            } else {
                chip.setBackgroundResource(R.drawable.bg_chip)
                chip.setTextColor(Color.parseColor("#3B2314"))
                chip.setTypeface(null, Typeface.NORMAL)
            }
        }
    }

    private fun setupQuantityStepper() {
        binding.btnMinus.setOnClickListener {
            if (quantity > 1) {
                quantity--
                binding.tvQuantity.text = quantity.toString()
                updatePriceDisplay()
            }
        }

        binding.btnPlus.setOnClickListener {
            if (quantity < 99) {
                quantity++
                binding.tvQuantity.text = quantity.toString()
                updatePriceDisplay()
            }
        }
    }

    private fun updatePriceDisplay() {
        val totalPrice = basePrice * quantity
        binding.tvTotalPrice.text = String.format(Locale.US, "$%.2f", totalPrice)
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
                    Toast.makeText(this, "Ditambahkan ke menu Favorit", Toast.LENGTH_SHORT).show()
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
