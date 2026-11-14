package com.example.caca_pie.pertemuan_10.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.caca_pie.databinding.ItemOrderHistoryBinding
import com.example.caca_pie.pertemuan_10.model.OrderHistoryModel

class OrderHistoryAdapter(
    context: Context,
    private val orderList: List<OrderHistoryModel>
) : ArrayAdapter<OrderHistoryModel>(context, 0, orderList) {

    private class ViewHolder(val binding: ItemOrderHistoryBinding)

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

        return view
    }
}
