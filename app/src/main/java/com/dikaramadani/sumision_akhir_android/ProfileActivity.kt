package com.dikaramadani.sumision_akhir_android

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.dikaramadani.sumision_akhir_android.databinding.ActivityProfileBinding
import java.io.File

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var dbHelper: DatabaseHelper
    private var currentUser: User? = null // PERUBAHAN: Simpan objek user saat ini

    // ▼▼▼ LANGKAH 1: Siapkan ActivityResultLauncher ▼▼▼
    private val editProfileLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // Blok ini akan dijalankan saat kembali dari EditProfileActivity
        if (result.resultCode == RESULT_OK) {
            // Jika hasilnya OK (artinya ada perubahan), muat ulang data pengguna
            loadAndDisplayUserData()
            Toast.makeText(this, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show()
        }
    }
    // ▲▲▲ Selesai Langkah 1 ▲▲▲

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)

        setSupportActionBar(binding.toolbarProfile)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Profil Saya"

        loadAndDisplayUserData()

        // ▼▼▼ LANGKAH 2: Beri aksi pada tombol FAB Edit ▼▼▼
        binding.fabEditProfile.setOnClickListener {
            // Pastikan data pengguna sudah dimuat sebelum pindah
            currentUser?.let { user ->
                val intent = Intent(this, EditProfileActivity::class.java)
                // Kirim seluruh objek user ke EditProfileActivity
                intent.putExtra("USER_TO_EDIT", user)
                // Gunakan launcher untuk memulai activity
                editProfileLauncher.launch(intent)
            }
        }
        // ▲▲▲ Selesai Langkah 2 ▲▲▲
    }

    private fun loadAndDisplayUserData() {
        val sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("USER_EMAIL", null)

        if (userEmail != null) {
            // Simpan hasil pencarian user ke properti kelas
            currentUser = dbHelper.getUserByEmail(userEmail)

            // Tampilkan data jika user ditemukan
            currentUser?.let { user ->
                binding.tvProfileName.text = user.username
                binding.tvProfileDescription.text = user.email

                // PERUBAHAN: Logika untuk menampilkan foto profil
                // (Ini akan kita lengkapi setelah kolom foto ditambahkan ke DB)

                if (user.photo.isNotEmpty()) {
                    binding.ivProfileAvatar.setImageURI(Uri.fromFile(File(user.photo)))
                } else {
                    binding.ivProfileAvatar.setImageResource(R.drawable.ic_person)
                }

            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
