package com.dikaramadani.sumision_akhir_android

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    // Deklarasikan DatabaseHelper sebagai properti kelas agar bisa diakses di semua fungsi
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inisialisasi DatabaseHelper
        dbHelper = DatabaseHelper(this)

        // 1. Hubungkan variabel dengan komponen di layout (XML)
        val etUsername = findViewById<EditText>(R.id.et_username_register)
        val etEmail = findViewById<EditText>(R.id.et_email_register)
        val etPassword = findViewById<EditText>(R.id.et_password_register)
        val etAlamat = findViewById<EditText>(R.id.et_alamat_register)
        val btnRegister = findViewById<Button>(R.id.btn_register)
        val tvLoginLink = findViewById<TextView>(R.id.tv_login_link)

        // 2. Tambahkan aksi saat tombol "Daftar" diklik
        btnRegister.setOnClickListener {
            // Ambil semua teks dari setiap EditText
            val username = etUsername.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val alamat = etAlamat.text.toString().trim()

            // 3. Lakukan validasi input
            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || alamat.isEmpty()) {
                // Jika ada salah satu input yang kosong, tampilkan pesan error
                Toast.makeText(this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show()
            } else {
                // Jika semua input terisi, coba tambahkan pengguna ke database
                val result = dbHelper.addUser(username, password, email, alamat)

                if (result > -1) {
                    // Jika result lebih dari -1, artinya pengguna berhasil ditambahkan
                    Toast.makeText(this, "Registrasi Berhasil!", Toast.LENGTH_SHORT).show()
                    // Setelah berhasil, arahkan pengguna ke halaman Login
                    // Kita akan buat LoginActivity di langkah berikutnya
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish() // Tutup RegisterActivity agar tidak bisa kembali dengan tombol back
                } else {
                    // Jika result adalah -1, artinya terjadi error saat insert (mungkin email sudah ada jika kita set UNIQUE)
                    Toast.makeText(this, "Registrasi Gagal, coba lagi", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // 4. Tambahkan aksi untuk link "Masuk di sini"
        tvLoginLink.setOnClickListener {
            // Pindah ke LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
