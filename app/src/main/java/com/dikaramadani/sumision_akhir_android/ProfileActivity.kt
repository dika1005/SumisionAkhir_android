package com.dikaramadani.sumision_akhir_android

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.dikaramadani.sumision_akhir_android.databinding.ActivityProfileBinding
import java.io.File

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var dbHelper: DatabaseHelper
    private var currentUser: User? = null

    private val editProfileLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            loadAndDisplayUserData()
            Toast.makeText(this, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)

        setSupportActionBar(binding.toolbarProfile)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Profil"

        loadAndDisplayUserData()

        binding.fabEditProfile.setOnClickListener { 
            currentUser?.let { user ->
                val intent = Intent(this, EditProfileActivity::class.java)
                intent.putExtra("USER_TO_EDIT", user)
                editProfileLauncher.launch(intent)
            }
        }

        binding.btnTransactionHistory.setOnClickListener { 
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadAndDisplayUserData() {
        val sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("USER_EMAIL", null)

        if (userEmail != null) {
            currentUser = dbHelper.getUserByEmail(userEmail)

            currentUser?.let { user ->
                supportActionBar?.title = user.username
                binding.tvProfileName.text = user.username
                binding.tvProfileEmail.text = user.email
                binding.tvProfileAddress.text = if (user.alamat.isNotEmpty()) user.alamat else "Alamat belum diatur"

                if (user.photo.isNotEmpty()) {
                    val photoUri = Uri.fromFile(File(user.photo))
                    binding.ivProfileAvatar.setImageURI(photoUri)
                    binding.ivProfileHeader.setImageURI(photoUri)
                } else {
                    binding.ivProfileAvatar.setImageResource(R.drawable.ic_person)
                    binding.ivProfileHeader.setImageResource(R.drawable.bg_profile_header)
                }

                // LOGIKA UNTUK MENAMPILKAN/MENYEMBUNYIKAN TOMBOL HISTORY
                if (user.role == "admin") {
                    binding.btnTransactionHistory.visibility = View.VISIBLE
                    binding.btnTransactionHistory.text = "Kelola Riwayat Transaksi"
                } else {
                    binding.btnTransactionHistory.visibility = View.GONE
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
