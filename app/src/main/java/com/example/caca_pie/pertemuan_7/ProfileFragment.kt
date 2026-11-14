package com.example.caca_pie.pertemuan_7

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.caca_pie.R
import com.example.caca_pie.databinding.FragmentProfileBinding
import com.example.caca_pie.pertemuan_10.OrderHistoryActivity
import com.example.caca_pie.pertemuan_11.onboarding.OnboardingActivity

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

        // Menu Order History (ListView CustomAdapter - Modul 10)
        binding.cardOrderHistory.setOnClickListener {
            startActivity(Intent(requireContext(), OrderHistoryActivity::class.java))
        }

        // Menu Tutorial / Onboarding (ViewPager2 - Modul 11)
        binding.cardTutorial.setOnClickListener {
            startActivity(Intent(requireContext(), OnboardingActivity::class.java))
        }

        // Social Links
        binding.cardInstagram.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://instagram.com/salsabilladheawan"))
            startActivity(intent)
        }

        binding.cardGithub.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/salsabilladheawan4"))
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
