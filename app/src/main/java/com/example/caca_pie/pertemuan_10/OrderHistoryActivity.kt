package com.example.caca_pie.pertemuan_10

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.caca_pie.R
import com.example.caca_pie.data.api.SupabaseApiClient
import com.example.caca_pie.data.model.OrderModel
import com.example.caca_pie.databinding.ActivityOrderHistoryBinding
import com.example.caca_pie.pertemuan_10.adapter.OrderHistoryTabsAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrderHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderHistoryBinding
    private val statusOptions = listOf("Dalam Proses", "Selesai")

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

        // FAB Tambah Pesanan Baru (Create ke Supabase - Modul 12)
        binding.fabAddOrder.setOnClickListener {
            showOrderFormDialog(null)
        }
    }

    // CREATE & UPDATE: Form Dialog untuk tambah / edit pesanan di Supabase
    fun showOrderFormDialog(existingOrder: OrderModel? = null) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_order_form, null)
        val tvTitle = dialogView.findViewById<TextView>(R.id.tvDialogOrderTitle)
        val etCode = dialogView.findViewById<EditText>(R.id.etOrderCode)
        val etItems = dialogView.findViewById<EditText>(R.id.etOrderItems)
        val etDate = dialogView.findViewById<EditText>(R.id.etOrderDate)
        val etTotal = dialogView.findViewById<EditText>(R.id.etOrderTotal)
        val spinnerStatus = dialogView.findViewById<Spinner>(R.id.spinnerOrderStatus)

        val statusAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            statusOptions
        )
        spinnerStatus.adapter = statusAdapter

        if (existingOrder != null) {
            tvTitle.text = "Edit Pesanan"
            etCode.setText(existingOrder.orderId)
            etItems.setText(existingOrder.itemsSummary)
            etDate.setText(existingOrder.date)
            etTotal.setText(existingOrder.totalAmount)
            val idx = statusOptions.indexOfFirst { it.equals(existingOrder.status, ignoreCase = true) }
            if (idx >= 0) spinnerStatus.setSelection(idx)
        } else {
            tvTitle.text = "Buat Pesanan Baru"
            val randomCode = "#JB-${(8300..8999).random()}"
            val nowStr = SimpleDateFormat("dd MMM yyyy • HH:mm 'WIB'", Locale.US).format(Date())
            etCode.setText(randomCode)
            etDate.setText(nowStr)
            val defaultTabStatus = if (binding.viewPagerOrders.currentItem == 0) 1 else 0
            spinnerStatus.setSelection(defaultTabStatus)
        }

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton(if (existingOrder == null) "Simpan" else "Perbarui", null)
            .setNegativeButton("Batal") { d, _ -> d.dismiss() }
            .create()

        dialog.setOnShowListener {
            val btnSave = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            btnSave.setTextColor(Color.parseColor("#8B4314"))
            btnSave.setOnClickListener {
                val orderId = etCode.text.toString().trim()
                val items = etItems.text.toString().trim()
                val date = etDate.text.toString().trim()
                val total = etTotal.text.toString().trim()
                val status = statusOptions[spinnerStatus.selectedItemPosition]

                if (orderId.isEmpty() || items.isEmpty() || total.isEmpty()) {
                    Toast.makeText(this, "Kode, rincian item, dan total harga wajib diisi", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val payload = OrderModel(
                    orderId = orderId,
                    date = date.ifEmpty { "Hari ini" },
                    itemsSummary = items,
                    totalAmount = total,
                    status = status
                )

                btnSave.isEnabled = false
                if (existingOrder?.id == null) {
                    createOrderInSupabase(payload, dialog, btnSave)
                } else {
                    updateOrderInSupabase(existingOrder.id, payload, dialog, btnSave)
                }
            }
        }
        dialog.show()
    }

    private fun createOrderInSupabase(order: OrderModel, dialog: AlertDialog, btnSave: View) {
        lifecycleScope.launch {
            try {
                val response = SupabaseApiClient.apiService.createOrder(order)
                if (response.isSuccessful) {
                    Toast.makeText(this@OrderHistoryActivity, "Pesanan ${order.orderId} ditambahkan", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                    refreshAllTabs()
                } else {
                    btnSave.isEnabled = true
                    Toast.makeText(this@OrderHistoryActivity, "Gagal menyimpan (Kode ${response.code()})", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                btnSave.isEnabled = true
                Toast.makeText(this@OrderHistoryActivity, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateOrderInSupabase(id: Long, order: OrderModel, dialog: AlertDialog, btnSave: View) {
        lifecycleScope.launch {
            try {
                val response = SupabaseApiClient.apiService.updateOrder("eq.$id", order)
                if (response.isSuccessful) {
                    Toast.makeText(this@OrderHistoryActivity, "Pesanan ${order.orderId} diperbarui", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                    refreshAllTabs()
                } else {
                    btnSave.isEnabled = true
                    Toast.makeText(this@OrderHistoryActivity, "Gagal memperbarui (Kode ${response.code()})", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                btnSave.isEnabled = true
                Toast.makeText(this@OrderHistoryActivity, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // DELETE: Konfirmasi & hapus pesanan di Supabase
    fun confirmDeleteOrder(order: OrderModel) {
        val dbId = order.id ?: return
        AlertDialog.Builder(this)
            .setTitle("Hapus Pesanan")
            .setMessage("Yakin ingin menghapus riwayat pesanan ${order.orderId}?")
            .setPositiveButton("Hapus") { _, _ ->
                lifecycleScope.launch {
                    try {
                        val response = SupabaseApiClient.apiService.deleteOrder("eq.$dbId")
                        if (response.isSuccessful) {
                            Toast.makeText(this@OrderHistoryActivity, "Pesanan ${order.orderId} dihapus", Toast.LENGTH_SHORT).show()
                            refreshAllTabs()
                        } else {
                            Toast.makeText(this@OrderHistoryActivity, "Gagal menghapus (Kode ${response.code()})", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this@OrderHistoryActivity, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Batal") { d, _ -> d.dismiss() }
            .show()
    }

    private fun refreshAllTabs() {
        supportFragmentManager.fragments.forEach { fragment ->
            when (fragment) {
                is OrderCompletedFragment -> fragment.loadOrders()
                is OrderProcessingFragment -> fragment.loadOrders()
            }
        }
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
