package com.example.caca_pie.data.model

import com.google.gson.annotations.SerializedName

data class OrderModel(
    val id: Long? = null,
    @SerializedName("order_id")
    val orderId: String,
    val date: String,
    @SerializedName("items_summary")
    val itemsSummary: String,
    @SerializedName("total_amount")
    val totalAmount: String,
    val status: String = "Dalam Proses",
    @SerializedName("created_at")
    val createdAt: String? = null
)
