package com.example.caca_pie.pertemuan_11.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.caca_pie.BaseActivity
import com.example.caca_pie.R
import com.example.caca_pie.databinding.ActivityOnboardingBinding
import com.example.caca_pie.pertemuan_6.AuthActivity

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val fragments = listOf(
            Onboarding1Fragment(),
            Onboarding2Fragment(),
            Onboarding3Fragment()
        )

        val adapter = OnboardingAdapter(this, fragments)
        binding.vpOnboarding.adapter = adapter

        binding.vpOnboarding.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateIndicators(position)
                if (position == fragments.size - 1) {
                    binding.btnNext.text = "Mulai Sekarang"
                    binding.btnSkip.visibility = View.INVISIBLE
                } else {
                    binding.btnNext.text = "Lanjut"
                    binding.btnSkip.visibility = View.VISIBLE
                }
            }
        })

        binding.btnNext.setOnClickListener {
            val currentItem = binding.vpOnboarding.currentItem
            if (currentItem < fragments.size - 1) {
                binding.vpOnboarding.currentItem = currentItem + 1
            } else {
                finishOnboarding()
            }
        }

        binding.btnSkip.setOnClickListener {
            finishOnboarding()
        }
    }

    private fun updateIndicators(position: Int) {
        val dot1 = binding.dot1
        val dot2 = binding.dot2
        val dot3 = binding.dot3

        dot1.setBackgroundResource(if (position == 0) R.drawable.bg_onboarding_dot_active else R.drawable.bg_onboarding_dot_inactive)
        dot2.setBackgroundResource(if (position == 1) R.drawable.bg_onboarding_dot_active else R.drawable.bg_onboarding_dot_inactive)
        dot3.setBackgroundResource(if (position == 2) R.drawable.bg_onboarding_dot_active else R.drawable.bg_onboarding_dot_inactive)

        val activeWidth = (24 * resources.displayMetrics.density).toInt()
        val inactiveWidth = (8 * resources.displayMetrics.density).toInt()

        dot1.layoutParams.width = if (position == 0) activeWidth else inactiveWidth
        dot2.layoutParams.width = if (position == 1) activeWidth else inactiveWidth
        dot3.layoutParams.width = if (position == 2) activeWidth else inactiveWidth

        dot1.requestLayout()
        dot2.requestLayout()
        dot3.requestLayout()
    }

    private fun finishOnboarding() {
        val sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE)
        sharedPref.edit().putBoolean("has_seen_onboarding", true).apply()

        val isLogin = sharedPref.getBoolean("isLogin", false)
        val targetActivity = if (isLogin) BaseActivity::class.java else AuthActivity::class.java

        val intent = Intent(this, targetActivity)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
