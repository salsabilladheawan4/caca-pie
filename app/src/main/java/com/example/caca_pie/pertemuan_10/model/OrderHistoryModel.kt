package com.example.caca_pie.pertemuan_10.model

data class OrderHistoryModel(
    val orderId: String,
    val date: String,
    val itemsSummary: String,
    val totalAmount: String,
    val status: String
)
