package com.example.caca_pie.pertemuan_7

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.caca_pie.databinding.FragmentCartBinding
import java.util.Locale

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private val unitPrice = 20.99
    private val taxRate = 0.10

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    override fun onResume() {
        super.onResume()
        updateCartUI()
    }

    private fun setupListeners() {
        val prefs = requireActivity().getSharedPreferences("user_pref", Context.MODE_PRIVATE)

        // Tombol Tambah Kuantitas (+)
        binding.btnCartPlus.setOnClickListener {
            val currentQty = prefs.getInt("cart_cappuccino_qty", 1)
            val newQty = currentQty + 1
            prefs.edit().putInt("cart_cappuccino_qty", newQty).apply()
            updateCartUI()
        }

        // Tombol Kurangi Kuantitas (-)
        binding.btnCartMinus.setOnClickListener {
            val currentQty = prefs.getInt("cart_cappuccino_qty", 1)
            if (currentQty > 1) {
                val newQty = currentQty - 1
                prefs.edit().putInt("cart_cappuccino_qty", newQty).apply()
                updateCartUI()
            } else {
                Toast.makeText(requireContext(), "Minimal pembelian 1 cup. Tekan ikon tempat sampah untuk menghapus.", Toast.LENGTH_SHORT).show()
            }
        }

        // Tombol Hapus Produk (Sampah)
        binding.btnCartDelete.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Hapus dari Keranjang")
                .setMessage("Apakah Anda yakin ingin menghapus Velvet Cappuccino dari keranjang?")
                .setPositiveButton("Hapus") { _, _ ->
                    prefs.edit()
                        .putBoolean("cart_has_cappuccino", false)
                        .putInt("cart_cappuccino_qty", 0)
                        .apply()
                    Toast.makeText(requireContext(), "Item dihapus dari keranjang", Toast.LENGTH_SHORT).show()
                    updateCartUI()
                }
                .setNegativeButton("Batal") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        // Tombol Checkout / Pesan Sekarang
        binding.btnCheckout.setOnClickListener {
            val qty = prefs.getInt("cart_cappuccino_qty", 1)
            val subtotal = qty * unitPrice
            val tax = subtotal * taxRate
            val total = subtotal + tax
            val formattedTotal = String.format(Locale.US, "$%.2f", total)

            AlertDialog.Builder(requireContext())
                .setTitle("Pesanan Berhasil Dibuat! ☕")
                .setMessage("Terima kasih! Pesanan Anda ($qty cup Velvet Cappuccino) dengan total $formattedTotal telah kami terima. Mohon tunggu barista menyiapkan pesanan Anda.")
                .setPositiveButton("Selesai") { dialog, _ ->
                    // Kosongkan keranjang setelah checkout sukses
                    prefs.edit()
                        .putBoolean("cart_has_cappuccino", false)
                        .putInt("cart_cappuccino_qty", 0)
                        .apply()
                    updateCartUI()
                    dialog.dismiss()
                }
                .setCancelable(false)
                .show()
        }
    }

    private fun updateCartUI() {
        val prefs = requireActivity().getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        val hasCartItem = prefs.getBoolean("cart_has_cappuccino", false)
        val qty = prefs.getInt("cart_cappuccino_qty", 0)

        if (hasCartItem && qty > 0) {
            binding.layoutEmptyCart.visibility = View.GONE
            binding.layoutContentCart.visibility = View.VISIBLE

            // Update kuantitas
            binding.tvCartQty.text = qty.toString()

            // Hitung harga
            val subtotal = qty * unitPrice
            val tax = subtotal * taxRate
            val total = subtotal + tax

            binding.tvSummarySubtotal.text = String.format(Locale.US, "$%.2f", subtotal)
            binding.tvSummaryTax.text = String.format(Locale.US, "$%.2f", tax)
            binding.tvSummaryTotal.text = String.format(Locale.US, "$%.2f", total)
        } else {
            binding.layoutEmptyCart.visibility = View.VISIBLE
            binding.layoutContentCart.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
