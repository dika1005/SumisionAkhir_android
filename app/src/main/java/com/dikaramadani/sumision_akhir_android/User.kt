package com.dikaramadani.sumision_akhir_android

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: Int,
    val username: String,
    val email: String,
    val alamat: String,
    val photo: String,
    val role: String // TAMBAHKAN ROLE
) : Parcelable
