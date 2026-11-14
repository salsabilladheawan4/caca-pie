package com.example.caca_pie.pertemuan_10

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.caca_pie.databinding.FragmentOrderCompletedBinding
import com.example.caca_pie.pertemuan_10.adapter.OrderHistoryAdapter
import com.example.caca_pie.pertemuan_10.model.OrderHistoryModel

class OrderCompletedFragment : Fragment() {

    private var _binding: FragmentOrderCompletedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderCompletedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Data pesanan selesai untuk ListView dengan CustomAdapter (Modul 10)
        val dummyOrders = listOf(
            OrderHistoryModel(
                orderId = "#JB-8201",
                date = "12 Nov 2026 • 10:30 WIB",
                itemsSummary = "1x Velvet Cappuccino, 1x Apple Pie Special",
                totalAmount = "$45.99",
                status = "Selesai"
            ),
            OrderHistoryModel(
                orderId = "#JB-8195",
                date = "10 Nov 2026 • 15:45 WIB",
                itemsSummary = "2x Caramel Macchiato (Medium), 1x Croissant",
                totalAmount = "$55.50",
                status = "Selesai"
            ),
            OrderHistoryModel(
                orderId = "#JB-8142",
                date = "08 Nov 2026 • 09:15 WIB",
                itemsSummary = "1x Americano (Cold), 1x Butter Croissant",
                totalAmount = "$46.50",
                status = "Selesai"
            ),
            OrderHistoryModel(
                orderId = "#JB-8088",
                date = "04 Nov 2026 • 16:20 WIB",
                itemsSummary = "1x Spanish Latte, 1x Apple Pie Special",
                totalAmount = "$69.00",
                status = "Selesai"
            ),
            OrderHistoryModel(
                orderId = "#JB-8012",
                date = "01 Nov 2026 • 11:00 WIB",
                itemsSummary = "1x Velvet Cappuccino (Large)",
                totalAmount = "$20.99",
                status = "Selesai"
            )
        )

        val adapter = OrderHistoryAdapter(requireContext(), dummyOrders)
        binding.lvOrderHistory.adapter = adapter

        binding.lvOrderHistory.setOnItemClickListener { _, _, position, _ ->
            val order = dummyOrders[position]
            Toast.makeText(
                requireContext(),
                "Pesanan ${order.orderId} senilai ${order.totalAmount} (${order.status})",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
