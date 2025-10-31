package com.dikaramadani.sumision_akhir_android

import android.content.Intent
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    // Deklarasikan DatabaseHelper
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Baris ini penting untuk menghubungkan activity ini dengan layout XML-nya
        setContentView(R.layout.activity_login)

        // Inisialisasi DatabaseHelper
        dbHelper = DatabaseHelper(this)

        // 1. Hubungkan variabel dengan komponen di layout (XML)
        val etEmail = findViewById<EditText>(R.id.et_email_login)
        val etPassword = findViewById<EditText>(R.id.et_password_login)
        val btnLogin = findViewById<Button>(R.id.btn_login)
        val tvRegisterLink = findViewById<TextView>(R.id.tv_register_link)
        val tvForgotPassword = findViewById<TextView>(R.id.tv_forgot_password)

        // 2. Tambahkan aksi saat tombol "Login" diklik
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan Password harus diisi", Toast.LENGTH_SHORT).show()
            } else {
                // Panggil fungsi checkUser yang sekarang mengembalikan role
                val userRole = dbHelper.checkUser(email, password)

                if (userRole != null) {
                    // Jika userRole tidak null, artinya login berhasil
                    Toast.makeText(this, "Login Berhasil! Role: $userRole", Toast.LENGTH_SHORT).show()

                    // --- TAMBAHKAN BLOK INI UNTUK MENYIMPAN SESI ---
                    // 1. Buat atau akses SharedPreferences
                    val sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE)
                    // 2. Buka editor untuk menulis data
                    val editor = sharedPreferences.edit()
                    // 3. Simpan data yang diperlukan (misalnya, email dan status login)
                    editor.putString("USER_EMAIL", email)
                    editor.putString("USER_ROLE", userRole)
                    editor.putBoolean("IS_LOGGED_IN", true)
                    // 4. Terapkan perubahan
                    editor.apply()
                    // ---------------------------------------------

                    // Siapkan intent untuk pindah ke MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("USER_ROLE", userRole)
                    startActivity(intent)
                    finish()
                } else {
                    // Jika userRole null, artinya login gagal
                    Toast.makeText(this, "Email atau Password salah", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // 3. Tambahkan aksi untuk link "Daftar di sini"
        tvRegisterLink.setOnClickListener {
            // Pindah ke RegisterActivity
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        tvForgotPassword.setOnClickListener {
            // Pindah ke ForgotPasswordActivity saat ditekan
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }
}
