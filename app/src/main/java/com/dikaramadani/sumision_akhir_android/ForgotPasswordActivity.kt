package com.dikaramadani.sumision_akhir_android

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        dbHelper = DatabaseHelper(this)

        val etEmail = findViewById<EditText>(R.id.et_email_forgot)
        val btnNext = findViewById<Button>(R.id.btn_next_forgot)

        btnNext.setOnClickListener {
            val email = etEmail.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(this, "Email tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Cek apakah ada pengguna dengan email tersebut
            val user = dbHelper.getUserByEmail(email)

            if (user != null) {
                // ▼▼▼ PASTIKAN BAGIAN INI SUDAH BENAR ▼▼▼
                // Jika user ditemukan, pindah ke halaman verifikasi.

                // Buat Intent ke VERIFICATION ACTIVITY, bukan ke LoginActivity
                val intent = Intent(this, VerificationActivity::class.java)

                // Kirim seluruh objek User agar bisa diverifikasi di halaman selanjutnya
                intent.putExtra("USER_DATA", user)
                startActivity(intent)
                // ▲▲▲ SELESAI PERBAIKAN ▲▲▲

            } else {
                // Jika user tidak ditemukan
                Toast.makeText(this, "Email tidak terdaftar", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
