package com.example.caca_pie.pertemuan_7

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.caca_pie.R
import com.example.caca_pie.pertemuan_5.FifthActivity
import com.example.caca_pie.pertemuan_5.WebViewActivity
import com.example.caca_pie.pertemuan_6.AuthActivity

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Kartu Lihat Menu -> FifthActivity
        view.findViewById<View>(R.id.cardLihatMenu).setOnClickListener {
            startActivity(Intent(requireContext(), FifthActivity::class.java))
        }

        // Kartu Kunjungi Website -> WebViewActivity
        view.findViewById<View>(R.id.cardWebsite).setOnClickListener {
            startActivity(Intent(requireContext(), WebViewActivity::class.java))
        }

        // Kartu Logout
        view.findViewById<View>(R.id.cardLogout).setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Konfirmasi Logout")
                .setMessage("Apakah Anda yakin ingin keluar dari akun?")
                .setPositiveButton("Ya") { _, _ ->
                    val sharedPref = requireActivity()
                        .getSharedPreferences("user_pref", android.content.Context.MODE_PRIVATE)
                    sharedPref.edit().clear().apply()

                    val intent = Intent(requireContext(), AuthActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
                .setNegativeButton("Batal") { dialog, _ -> dialog.dismiss() }
                .show()
        }
    }
}
