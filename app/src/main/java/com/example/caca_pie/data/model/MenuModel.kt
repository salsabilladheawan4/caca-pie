package com.example.caca_pie.data.model

import com.example.caca_pie.R
import com.google.gson.annotations.SerializedName

data class MenuModel(
    val id: Long? = null,
    val name: String,
    val category: String = "Hot Coffee",
    val price: String,
    @SerializedName("original_price")
    val originalPrice: String = "",
    val tag: String = "",
    @SerializedName("image_key")
    val imageKey: String = "img_cappuccino",
    @SerializedName("created_at")
    val createdAt: String? = null
) {
    fun toDrawableRes(): Int {
        return when (imageKey) {
            "img_americano" -> R.drawable.img_americano
            "img_spanish_latte" -> R.drawable.img_spanish_latte
            "img_caramel_macchiato" -> R.drawable.img_caramel_macchiato
            "apple_pie" -> R.drawable.apple_pie
            else -> R.drawable.img_cappuccino
        }
    }
}
