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
import com.example.caca_pie.databinding.FragmentOrderCompletedBinding
import com.example.caca_pie.pertemuan_10.adapter.OrderHistoryAdapter
import kotlinx.coroutines.launch

class OrderCompletedFragment : Fragment() {

    private var _binding: FragmentOrderCompletedBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: OrderHistoryAdapter
    private val orders = mutableListOf<OrderModel>()

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
        binding.lvOrderHistory.adapter = adapter

        binding.lvOrderHistory.setOnItemClickListener { _, _, position, _ ->
            val order = orders[position]
            Toast.makeText(
                requireContext(),
                "Pesanan ${order.orderId} senilai ${order.totalAmount} (${order.status})",
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
        binding.pbOrdersLoading.visibility = View.VISIBLE
        binding.tvEmptyOrders.visibility = View.GONE

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = SupabaseApiClient.apiService.getOrders(statusFilter = "eq.Selesai")
                if (_binding == null) return@launch

                binding.pbOrdersLoading.visibility = View.GONE
                if (response.isSuccessful) {
                    val result = response.body() ?: emptyList()
                    adapter.updateData(result)
                    binding.tvEmptyOrders.visibility = if (result.isEmpty()) View.VISIBLE else View.GONE
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Gagal memuat pesanan selesai (Kode ${response.code()})",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                if (_binding == null) return@launch
                binding.pbOrdersLoading.visibility = View.GONE
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
