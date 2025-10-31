package com.dikaramadani.sumision_akhir_android

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private var userEmail: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        dbHelper = DatabaseHelper(this)

        // 1. Ambil email pengguna yang dikirim dari VerificationActivity
        userEmail = intent.getStringExtra("USER_EMAIL")

        // Jika email tidak ada, proses tidak bisa dilanjutkan
        if (userEmail == null) {
            Toast.makeText(this, "Terjadi kesalahan, email tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Hubungkan ke komponen UI
        val etNewPassword = findViewById<EditText>(R.id.et_new_password)
        val etConfirmPassword = findViewById<EditText>(R.id.et_confirm_password)
        val btnSave = findViewById<Button>(R.id.btn_save_password)

        // 2. Tambahkan aksi saat tombol "Simpan" diklik
        btnSave.setOnClickListener {
            val newPassword = etNewPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            // 3. Lakukan validasi
            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newPassword != confirmPassword) {
                Toast.makeText(this, "Password tidak cocok", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Jika validasi berhasil, update password di database
            // Kita gunakan userEmail yang sudah kita simpan untuk memastikan user yang benar
            val rowsAffected = dbHelper.updateUserPassword(userEmail!!, newPassword)

            if (rowsAffected > 0) {
                // Jika update berhasil (rowsAffected > 0)
                Toast.makeText(this, "Password berhasil diperbarui!", Toast.LENGTH_LONG).show()

                // Arahkan pengguna kembali ke halaman Login
                val intent = Intent(this, LoginActivity::class.java)
                // Hapus semua activity sebelumnya dari tumpukan (back stack)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            } else {
                // Jika update gagal
                Toast.makeText(this, "Gagal memperbarui password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
