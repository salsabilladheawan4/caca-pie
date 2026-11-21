package com.example.caca_pie.data.model

import com.google.gson.annotations.SerializedName

data class UserModel(
    val id: Long? = null,
    val nama: String,
    val email: String,
    @SerializedName("tanggal_lahir")
    val tanggalLahir: String,
    @SerializedName("jenis_kelamin")
    val jenisKelamin: String,
    val username: String,
    val password: String,
    @SerializedName("created_at")
    val createdAt: String? = null
)
