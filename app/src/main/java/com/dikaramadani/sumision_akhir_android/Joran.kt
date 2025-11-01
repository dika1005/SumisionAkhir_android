package com.dikaramadani.sumision_akhir_android

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Joran(
    val id: Int = 0,
    val name: String,
    val price: String,
    val description: String,
    val photo: String,
    val panjang: String,
    val power: String,
    val material: String,
    val aksi: String,
    val jenis: String,
    val guides: String,
    val handle: String,
    val category: String,
    val stok: Int
) : Parcelable