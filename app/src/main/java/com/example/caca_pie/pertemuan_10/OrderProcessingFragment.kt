package com.example.caca_pie.pertemuan_10

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.caca_pie.data.api.SupabaseApiClient
import com.example.caca_pie.data.model.OrderModel
import com.example.caca_pie.databinding.FragmentOrderProcessingBinding
import com.example.caca_pie.pertemuan_10.adapter.OrderHistoryAdapter
import kotlinx.coroutines.launch

class OrderProcessingFragment : Fragment() {

    private var _binding: FragmentOrderProcessingBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: OrderHistoryAdapter
    private val orders = mutableListOf<OrderModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderProcessingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = OrderHistoryAdapter(
            context = requireContext(),
            orderList = orders,
            onEditClick = { order ->
                (activity as? OrderHistoryActivity)?.showOrderFormDialog(order)
            },
            onDeleteClick = { order ->
                (activity as? OrderHistoryActivity)?.confirmDeleteOrder(order)
            }
        )
        binding.lvOrderProcessing.adapter = adapter

        binding.lvOrderProcessing.setOnItemClickListener { _, _, position, _ ->
            val order = orders[position]
            Toast.makeText(
                requireContext(),
                "Pesanan ${order.orderId} sedang diproses (${order.totalAmount})",
                Toast.LENGTH_SHORT
            ).show()
        }

        loadOrders()
    }

    override fun onResume() {
        super.onResume()
        loadOrders()
    }

    fun loadOrders() {
        if (_binding == null) return
        binding.pbOrdersProcessingLoading.visibility = View.VISIBLE
        binding.tvEmptyOrdersProcessing.visibility = View.GONE

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = SupabaseApiClient.apiService.getOrders(statusFilter = "eq.Dalam Proses")
                if (_binding == null) return@launch

                binding.pbOrdersProcessingLoading.visibility = View.GONE
                if (response.isSuccessful) {
                    val result = response.body() ?: emptyList()
                    adapter.updateData(result)
                    binding.tvEmptyOrdersProcessing.visibility = if (result.isEmpty()) View.VISIBLE else View.GONE
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Gagal memuat pesanan aktif (Kode ${response.code()})",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                if (_binding == null) return@launch
                binding.pbOrdersProcessingLoading.visibility = View.GONE
                Toast.makeText(
                    requireContext(),
                    "Gagal terhubung ke server: ${e.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
