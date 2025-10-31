package com.dikaramadani.sumision_akhir_android

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.dikaramadani.sumision_akhir_android.databinding.ActivityEditProfileBinding
import java.io.File
import java.io.FileOutputStream

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var dbHelper: DatabaseHelper
    private var userToEdit: User? = null
    private var newPhotoPath: String? = null

    // Launcher untuk memilih gambar dari galeri
    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            // Tampilkan gambar yang baru dipilih di ImageView
            binding.ivEditAvatar.setImageURI(it)
            // Simpan gambar ke penyimpanan internal dan dapatkan path-nya
            newPhotoPath = saveImageToInternalStorage(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)

        // 1. Ambil data User yang dikirim dari ProfileActivity
        userToEdit = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("USER_TO_EDIT", User::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("USER_TO_EDIT")
        }

        // Jika data user ada, isi semua form dengan data tersebut
        userToEdit?.let { user ->
            binding.etEditUsername.setText(user.username)
            binding.etEditAlamat.setText(user.alamat)
            binding.etEditEmail.setText(user.email) // Email ditampilkan tapi tidak bisa diedit

            // Tampilkan foto profil lama jika ada
            // (Kita akan aktifkan setelah DB di-update)

            if (user.photo.isNotEmpty()) {
                binding.ivEditAvatar.setImageURI(Uri.fromFile(File(user.photo)))
            }

        }

        // 2. Beri aksi pada tombol "Ganti Foto"
        binding.btnChangePhoto.setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        // 3. Beri aksi pada tombol "Simpan Perubahan"
        binding.btnSaveProfile.setOnClickListener {
            saveChanges()
        }
    }

    // Di dalam file EditProfileActivity.kt

    private fun saveChanges() {
        val newUsername = binding.etEditUsername.text.toString().trim()
        val newAlamat = binding.etEditAlamat.text.toString().trim()

        // Pastikan userToEdit tidak null
        userToEdit?.let { oldUser ->
            // ▼▼▼ INI PERUBAHAN UTAMANYA ▼▼▼
            // Pilih path foto yang akan disimpan:
            // - Gunakan path foto yang BARU jika pengguna memilih foto baru (newPhotoPath tidak null).
            // - Jika tidak, gunakan path foto yang LAMA (oldUser.photo).
            val photoToSave = newPhotoPath ?: oldUser.photo

            // Panggil fungsi updateUser dari DatabaseHelper dengan 5 parameter
            dbHelper.updateUser(
                oldUser.id.toString(),
                newUsername,
                oldUser.email,
                newAlamat,
                photoToSave  // <-- Sertakan foto yang akan disimpan
            )
            // ▲▲▲ SELESAI PERUBAHAN ▲▲▲

            // Beri sinyal ke ProfileActivity bahwa update berhasil
            setResult(Activity.RESULT_OK)
            // Tutup halaman edit
            finish()
        }
    }


    // Fungsi ini sama persis dengan yang ada di AddEditActivity, bisa di-copy-paste
    private fun saveImageToInternalStorage(uri: Uri): String? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            val fileName = "profile_${System.currentTimeMillis()}.jpg"
            val file = File(filesDir, fileName)
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            outputStream.close()
            inputStream?.close()
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
