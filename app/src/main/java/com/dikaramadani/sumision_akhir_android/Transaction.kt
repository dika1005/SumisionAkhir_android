package com.dikaramadani.sumision_akhir_android

// Data class untuk menampung riwayat transaksi
data class Transaction(
    val id: Int,
    val userId: Int,
    val details: String, // Detail barang dalam format Teks/JSON
    val totalPrice: String,
    val transactionDate: String
)
    