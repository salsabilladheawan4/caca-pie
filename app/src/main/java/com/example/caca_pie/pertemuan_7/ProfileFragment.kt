package com.example.caca_pie.pertemuan_7

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.caca_pie.R
import com.example.caca_pie.databinding.FragmentProfileBinding
import com.example.caca_pie.pertemuan_10.OrderHistoryActivity
import com.example.caca_pie.pertemuan_11.onboarding.OnboardingActivity
import com.example.caca_pie.pertemuan_6.AuthActivity

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireContext().getSharedPreferences("user_pref", Context.MODE_PRIVATE)

        // Tampilkan info akun pengguna Supabase yang sedang login
        val nama = sharedPref.getString("nama", "") ?: ""
        val username = sharedPref.getString("username", "") ?: ""
        val displayName = when {
            nama.isNotBlank() && username.isNotBlank() -> "$nama (@$username)"
            nama.isNotBlank() -> nama
            username.isNotBlank() -> "@$username"
            else -> "Guest User"
        }
        binding.tvAccountName.text = displayName

        // Menu Order History (ListView CustomAdapter - Modul 10)
        binding.cardOrderHistory.setOnClickListener {
            startActivity(Intent(requireContext(), OrderHistoryActivity::class.java))
        }

        // Menu Tutorial / Onboarding (ViewPager2 - Modul 11)
        binding.cardTutorial.setOnClickListener {
            startActivity(Intent(requireContext(), OnboardingActivity::class.java))
        }

        // Social Links Pengembang
        binding.cardInstagram.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://instagram.com/salsabilladheawan"))
            startActivity(intent)
        }

        binding.cardGithub.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/salsabilladheawan4"))
            startActivity(intent)
        }

        // Fitur Logout
        binding.cardLogout.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Konfirmasi Logout")
                .setMessage("Apakah Anda yakin ingin keluar dari akun?")
                .setPositiveButton("Keluar") { _, _ ->
                    sharedPref.edit()
                        .putBoolean("isLogin", false)
                        .apply()
                    Toast.makeText(requireContext(), "Berhasil logout", Toast.LENGTH_SHORT).show()
                    val intent = Intent(requireContext(), AuthActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    requireActivity().finish()
                }
                .setNegativeButton("Batal", null)
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
