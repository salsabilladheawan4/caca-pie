package com.example.caca_pie.pertemuan_10.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.caca_pie.pertemuan_10.OrderCompletedFragment
import com.example.caca_pie.pertemuan_10.OrderProcessingFragment

class OrderHistoryTabsAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OrderCompletedFragment()
            else -> OrderProcessingFragment()
        }
    }
}
