package com.example.caca_pie.pertemuan_11.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.caca_pie.data.model.MenuModel
import com.example.caca_pie.databinding.ItemProductBinding

class ProductAdapter(
    private var productList: List<MenuModel>,
    private val onItemClick: (MenuModel) -> Unit,
    private val onEditClick: (MenuModel) -> Unit,
    private val onDeleteClick: (MenuModel) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun updateData(newList: List<MenuModel>) {
        productList = newList
        notifyDataSetChanged()
    }

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
            imgProduct.setImageResource(product.toDrawableRes())

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

            btnMenuMore.setOnClickListener { anchor ->
                val popup = PopupMenu(anchor.context, anchor)
                popup.menu.add(0, 1, 0, "Edit Menu")
                popup.menu.add(0, 2, 1, "Hapus Menu")
                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        1 -> {
                            onEditClick(product)
                            true
                        }
                        2 -> {
                            onDeleteClick(product)
                            true
                        }
                        else -> false
                    }
                }
                popup.show()
            }
        }
    }

    override fun getItemCount(): Int = productList.size
}
