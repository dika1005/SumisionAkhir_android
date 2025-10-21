package com.dikaramadani.sumision_akhir_android

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Joran(
    val name: String,
    val price: String,
    val description: String,
    val photo: Int,
    val panjang: String? = null,
    val power: String? = null,
    val material: String? = null,
    val aksi: String? = null,
    val jenis: String? = null,
    val guides: String? = null,
    val handle: String? = null
) : Parcelable