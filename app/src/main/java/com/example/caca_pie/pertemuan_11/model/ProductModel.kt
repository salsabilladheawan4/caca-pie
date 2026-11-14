package com.example.caca_pie.pertemuan_11.model

data class ProductModel(
    val id: Int,
    val name: String,
    val price: String,
    val originalPrice: String = "",
    val imageRes: Int,
    val tag: String = ""
)
