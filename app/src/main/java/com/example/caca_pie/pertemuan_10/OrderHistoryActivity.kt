package com.example.caca_pie.pertemuan_10

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.caca_pie.R
import com.example.caca_pie.databinding.ActivityOrderHistoryBinding
import com.example.caca_pie.pertemuan_10.adapter.OrderHistoryTabsAdapter
import com.google.android.material.tabs.TabLayoutMediator

class OrderHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOrderHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = "Riwayat Pesanan"
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        }

        // Hubungkan Adapter ke ViewPager2 (Modul 11)
        val tabsAdapter = OrderHistoryTabsAdapter(this)
        binding.viewPagerOrders.adapter = tabsAdapter

        // Integrasikan TabLayout dan ViewPager2 menggunakan TabLayoutMediator (Modul 11)
        TabLayoutMediator(binding.tabLayoutOrders, binding.viewPagerOrders) { tab, position ->
            tab.text = when (position) {
                0 -> "Selesai"
                else -> "Dalam Proses"
            }
        }.attach()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
