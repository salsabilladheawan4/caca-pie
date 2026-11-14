package com.example.caca_pie.pertemuan_7

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.caca_pie.R
import com.example.caca_pie.databinding.FragmentHomeBinding
import com.example.caca_pie.pertemuan_3.DetailActivity
import com.example.caca_pie.pertemuan_5.FifthActivity
import com.example.caca_pie.pertemuan_5.WebViewActivity
import com.example.caca_pie.pertemuan_6.AuthActivity
import com.example.caca_pie.pertemuan_11.adapter.ProductAdapter
import com.example.caca_pie.pertemuan_11.model.ProductModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupListeners()
    }

    private fun setupRecyclerView() {
        // Data Produk sesuai GrandDesain Figma Jambang Cafe
        val productList = listOf(
            ProductModel(
                id = 1,
                name = "Cappuccino",
                price = "$40",
                originalPrice = "$48",
                imageRes = R.drawable.img_cappuccino,
                tag = "TOP SALE"
            ),
            ProductModel(
                id = 2,
                name = "Americano",
                price = "$40",
                originalPrice = "$48",
                imageRes = R.drawable.img_americano,
                tag = "5% OFF"
            ),
            ProductModel(
                id = 3,
                name = "Spanish Latte",
                price = "$44",
                originalPrice = "$50",
                imageRes = R.drawable.img_spanish_latte,
                tag = "POPULAR"
            ),
            ProductModel(
                id = 4,
                name = "Caramel Macchiato",
                price = "$48",
                originalPrice = "$54",
                imageRes = R.drawable.img_caramel_macchiato,
                tag = "HOT"
            ),
            ProductModel(
                id = 5,
                name = "Velvet Cappuccino",
                price = "$20.99",
                originalPrice = "",
                imageRes = R.drawable.img_cappuccino,
                tag = "SIGNATURE"
            ),
            ProductModel(
                id = 6,
                name = "Apple Pie Special",
                price = "$25.00",
                originalPrice = "",
                imageRes = R.drawable.apple_pie,
                tag = "BAKERY"
            )
        )

        // Grid 2 Kolom menggunakan GridLayoutManager (Modul 11)
        binding.rvProducts.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvProducts.isNestedScrollingEnabled = false
        binding.rvProducts.setHasFixedSize(true)
        val adapter = ProductAdapter(productList) { product ->
            if (product.id == 6) {
                // Apple Pie membuka DetailActivity
                startActivity(Intent(requireContext(), DetailActivity::class.java))
            } else {
                // Produk Kopi membuka FifthActivity (Details)
                startActivity(Intent(requireContext(), FifthActivity::class.java))
            }
        }
        binding.rvProducts.adapter = adapter
    }

    private fun setupListeners() {
        // Banner Promo "Order Now"
        binding.btnOrderNow.setOnClickListener {
            startActivity(Intent(requireContext(), FifthActivity::class.java))
        }

        // Tombol Kunjungi Website (Sesuai Figma)
        binding.cardWebsite.setOnClickListener {
            startActivity(Intent(requireContext(), WebViewActivity::class.java))
        }

        // Filter Kategori Chips
        binding.chipAll.setOnClickListener {
            selectChip(binding.chipAll)
        }
        binding.chipHotCoffee.setOnClickListener {
            selectChip(binding.chipHotCoffee)
        }
        binding.chipColdCoffee.setOnClickListener {
            selectChip(binding.chipColdCoffee)
        }
        binding.chipPastry.setOnClickListener {
            selectChip(binding.chipPastry)
        }

        // Tombol Logout
        binding.cardLogout.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Konfirmasi Logout")
                .setMessage("Apakah Anda yakin ingin keluar dari akun?")
                .setPositiveButton("Ya") { _, _ ->
                    val sharedPref = requireActivity()
                        .getSharedPreferences("user_pref", Context.MODE_PRIVATE)
                    sharedPref.edit().putBoolean("isLogin", false).apply()

                    val intent = Intent(requireContext(), AuthActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
                .setNegativeButton("Batal") { dialog, _ -> dialog.dismiss() }
                .show()
        }
    }

    private fun selectChip(selectedView: View) {
        val chips = listOf(binding.chipAll, binding.chipHotCoffee, binding.chipColdCoffee, binding.chipPastry)
        for (chip in chips) {
            if (chip == selectedView) {
                chip.setBackgroundResource(R.drawable.bg_chip_selected)
                chip.setTextColor(android.graphics.Color.WHITE)
                chip.setTypeface(null, android.graphics.Typeface.BOLD)
            } else {
                chip.setBackgroundResource(R.drawable.bg_chip)
                chip.setTextColor(android.graphics.Color.parseColor("#3B2314"))
                chip.setTypeface(null, android.graphics.Typeface.NORMAL)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
