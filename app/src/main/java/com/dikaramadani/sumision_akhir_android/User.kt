package com.dikaramadani.sumision_akhir_android

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: Int = 0,
    val username: String,
    val email: String,
    val alamat: String,
    val photo: String
) : Parcelable
    