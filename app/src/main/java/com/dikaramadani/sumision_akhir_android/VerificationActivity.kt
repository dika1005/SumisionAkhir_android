package com.dikaramadani.sumision_akhir_android

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class VerificationActivity : AppCompatActivity() {

    // Variabel untuk menampung data user yang dikirim dari activity sebelumnya
    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)

        // 1. Ambil data User yang dikirim dari ForgotPasswordActivity
        user = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("USER_DATA", User::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("USER_DATA")
        }

        // Jika karena suatu hal data user tidak ada, tutup activity ini
        if (user == null) {
            Toast.makeText(this, "Terjadi kesalahan, coba lagi", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Hubungkan ke komponen UI
        val etUsername = findViewById<EditText>(R.id.et_username_verification)
        val btnVerify = findViewById<Button>(R.id.btn_verify)

        // 2. Tambahkan aksi saat tombol "Verifikasi" diklik
        btnVerify.setOnClickListener {
            val inputUsername = etUsername.text.toString().trim()

            // 3. Bandingkan input pengguna dengan username yang benar
            if (inputUsername.equals(user?.username, ignoreCase = true)) {
                // Jika username cocok, verifikasi berhasil
                Toast.makeText(this, "Verifikasi Berhasil!", Toast.LENGTH_SHORT).show()

                // Pindah ke halaman untuk mengatur password baru
                // Kirim email pengguna agar kita tahu akun mana yang akan di-update
                val intent = Intent(this, ResetPasswordActivity::class.java)
                intent.putExtra("USER_EMAIL", user?.email)
                startActivity(intent)
                finish() // Tutup activity ini
            } else {
                // Jika username tidak cocok
                Toast.makeText(this, "Username tidak cocok", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
