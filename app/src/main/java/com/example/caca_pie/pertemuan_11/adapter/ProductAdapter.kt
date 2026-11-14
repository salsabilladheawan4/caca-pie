package com.example.caca_pie.pertemuan_11.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.caca_pie.databinding.ItemProductBinding
import com.example.caca_pie.pertemuan_11.model.ProductModel

class ProductAdapter(
    private val productList: List<ProductModel>,
    private val onItemClick: (ProductModel) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        with(holder.binding) {
            tvProductName.text = product.name
            tvProductPrice.text = product.price
            imgProduct.setImageResource(product.imageRes)

            if (product.tag.isNotEmpty()) {
                tvTag.visibility = View.VISIBLE
                tvTag.text = product.tag
            } else {
                tvTag.visibility = View.GONE
            }

            if (product.originalPrice.isNotEmpty()) {
                tvOriginalPrice.visibility = View.VISIBLE
                tvOriginalPrice.text = product.originalPrice
                tvOriginalPrice.paintFlags = tvOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                tvOriginalPrice.visibility = View.GONE
            }

            root.setOnClickListener {
                onItemClick(product)
            }
        }
    }

    override fun getItemCount(): Int = productList.size
}
