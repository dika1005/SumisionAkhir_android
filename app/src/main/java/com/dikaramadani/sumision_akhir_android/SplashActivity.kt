package com.dikaramadani.sumision_akhir_android

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

// Hapus import Handler dan Looper karena tidak dipakai lagi
// import android.os.Handler
// import android.os.Looper

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Kita bahkan tidak perlu setContentView karena activity ini hanya sebagai pengarah
        // setContentView(R.layout.activity_splash)

        // 1. Akses SharedPreferences untuk memeriksa sesi
        val sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE)

        // 2. Ambil status login. Jika tidak ada, anggap false (belum login)
        val isLoggedIn = sharedPreferences.getBoolean("IS_LOGGED_IN", false)

        // 3. Tentukan tujuan berdasarkan status login
        if (isLoggedIn) {
            // JIKA SUDAH LOGIN:
            // Ambil juga role-nya agar bisa diteruskan ke MainActivity
            val userRole = sharedPreferences.getString("USER_ROLE", "user")

            // Buat intent untuk pindah ke MainActivity
            val intent = Intent(this, MainActivity::class.java)
            // Selipkan role pengguna
            intent.putExtra("USER_ROLE", userRole)
            startActivity(intent)
        } else {
            // JIKA BELUM LOGIN:
            // Buat intent untuk pindah ke LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // 4. Tutup SplashActivity ini agar tidak bisa diakses kembali dengan tombol "back"
        finish()
    }
}
