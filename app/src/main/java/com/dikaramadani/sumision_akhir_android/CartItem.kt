package com.dikaramadani.sumision_akhir_android

// Data class ini tidak perlu Parcelable kecuali Anda ingin mengirimnya antar Activity
data class CartItem(
    val id: Int,
    val userId: Int,
    val joranId: Int,
    var jumlah: Int,

    // Properti tambahan ini untuk mempermudah penampilan di UI
    // Kita akan mengisinya secara manual setelah mengambil data dari DB
    var joranName: String? = null,
    var joranPrice: String? = null,
    var joranPhoto: String? = null
)
    