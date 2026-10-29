package com.example.caca_pie.pertemuan_7

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.caca_pie.databinding.FragmentFavoriteBinding
import com.example.caca_pie.pertemuan_5.FifthActivity

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Tombol Lihat Detail -> Membuka FifthActivity
        binding.btnViewDetail.setOnClickListener {
            startActivity(Intent(requireContext(), FifthActivity::class.java))
        }

        // Tombol Hapus dari Favorit
        binding.btnRemoveFavorite.setOnClickListener {
            val prefs = requireActivity().getSharedPreferences("user_pref", Context.MODE_PRIVATE)
            prefs.edit().putBoolean("fav_cappuccino", false).apply()
            Toast.makeText(requireContext(), "Menu dihapus dari Favorit", Toast.LENGTH_SHORT).show()
            updateFavoriteUI()
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh tampilan setiap kali tab Favorite dibuka
        updateFavoriteUI()
    }

    private fun updateFavoriteUI() {
        val prefs = requireActivity().getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        val isFav = prefs.getBoolean("fav_cappuccino", false)

        if (isFav) {
            binding.layoutEmpty.visibility = View.GONE
            binding.layoutContent.visibility = View.VISIBLE
        } else {
            binding.layoutEmpty.visibility = View.VISIBLE
            binding.layoutContent.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
