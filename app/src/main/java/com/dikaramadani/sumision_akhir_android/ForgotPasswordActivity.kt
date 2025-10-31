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
                // Jika user ditemukan, pindah ke halaman verifikasi
                // Kirim data user (terutama username/alamat) untuk verifikasi
                val intent = Intent(this, VerificationActivity::class.java)
                intent.putExtra("USER_DATA", user) // Kirim seluruh objek User
                startActivity(intent)
            } else {
                // Jika user tidak ditemukan
                Toast.makeText(this, "Email tidak terdaftar", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
