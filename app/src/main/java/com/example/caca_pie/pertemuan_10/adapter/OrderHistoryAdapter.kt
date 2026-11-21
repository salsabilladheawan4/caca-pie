package com.example.caca_pie.pertemuan_10.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.caca_pie.R
import com.example.caca_pie.data.model.OrderModel
import com.example.caca_pie.databinding.ItemOrderHistoryBinding

class OrderHistoryAdapter(
    context: Context,
    private val orderList: MutableList<OrderModel>,
    private val onEditClick: (OrderModel) -> Unit,
    private val onDeleteClick: (OrderModel) -> Unit
) : ArrayAdapter<OrderModel>(context, 0, orderList) {

    private class ViewHolder(val binding: ItemOrderHistoryBinding)

    fun updateData(newOrders: List<OrderModel>) {
        orderList.clear()
        orderList.addAll(newOrders)
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: ItemOrderHistoryBinding
        val view: View

        if (convertView == null) {
            binding = ItemOrderHistoryBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
            view = binding.root
            view.tag = ViewHolder(binding)
        } else {
            view = convertView
            binding = (view.tag as ViewHolder).binding
        }

        val item = orderList[position]
        binding.tvOrderId.text = item.orderId
        binding.tvStatus.text = item.status
        binding.tvDate.text = item.date
        binding.tvItemsSummary.text = item.itemsSummary
        binding.tvTotalAmount.text = item.totalAmount

        if (item.status.equals("Selesai", ignoreCase = true)) {
            binding.tvStatus.setBackgroundResource(R.drawable.bg_badge_status)
            binding.tvStatus.setTextColor(Color.parseColor("#2E7D32"))
        } else {
            binding.tvStatus.setBackgroundResource(R.drawable.bg_badge_processing)
            binding.tvStatus.setTextColor(Color.parseColor("#E65100"))
        }

        binding.tvEditOrder.setOnClickListener {
            onEditClick(item)
        }

        binding.tvDeleteOrder.setOnClickListener {
            onDeleteClick(item)
        }

        return view
    }
}
