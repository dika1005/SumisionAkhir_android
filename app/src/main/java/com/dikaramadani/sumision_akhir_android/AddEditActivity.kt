package com.dikaramadani.sumision_akhir_android

import android.app.Activity
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.dikaramadani.sumision_akhir_android.databinding.ActivityAddEditBinding
import java.io.File
import java.io.FileOutputStream

class AddEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditBinding
    private lateinit var dbHelper: DatabaseHelper
    private var joran: Joran? = null
    private var imagePath: String? = null

    companion object {
        const val EXTRA_JORAN = "extra_joran"
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            imagePath = saveImageToInternalStorage(it)
            binding.ivPhoto.setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)

        val categories = arrayOf("Laut", "Danau", "Sungai", "Galatama")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, categories)
        binding.etCategory.setAdapter(adapter)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            joran = intent.getParcelableExtra(EXTRA_JORAN, Joran::class.java)
        } else {
            @Suppress("DEPRECATION")
            joran = intent.getParcelableExtra(EXTRA_JORAN)
        }

        if (joran != null) {
            supportActionBar?.title = "Edit Joran"
            binding.etName.setText(joran?.name)
            binding.etPrice.setText(joran?.price)
            binding.etStok.setText(joran?.stok.toString()) // Mengisi stok
            binding.etDescription.setText(joran?.description)
            binding.etPanjang.setText(joran?.panjang)
            // ▼▼▼ MELENGKAPI BAGIAN YANG HILANG SAAT EDIT ▼▼▼
            binding.etPower.setText(joran?.power)
            binding.etMaterial.setText(joran?.material)
            binding.etAksi.setText(joran?.aksi)
            binding.etJenis.setText(joran?.jenis)
            binding.etGuides.setText(joran?.guides)
            binding.etHandle.setText(joran?.handle)
            binding.etCategory.setText(joran?.category, false)

            if (joran!!.photo.isNotEmpty()) {
                imagePath = joran!!.photo
                binding.ivPhoto.setImageURI(Uri.fromFile(File(imagePath!!)))
            }
        } else {
            supportActionBar?.title = "Tambah Joran"
        }

        binding.btnChooseImage.setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        binding.btnSave.setOnClickListener {
            saveJoran()
        }
    }

    private fun saveJoran() {
        val name = binding.etName.text.toString()
        val price = binding.etPrice.text.toString()
        val stok = binding.etStok.text.toString().toIntOrNull() ?: 0
        val description = binding.etDescription.text.toString()

        // ▼▼▼ MELENGKAPI SEMUA VARIABEL YANG HILANG ▼▼▼
        val panjang = binding.etPanjang.text.toString()
        val power = binding.etPower.text.toString()
        val material = binding.etMaterial.text.toString()
        val aksi = binding.etAksi.text.toString()
        val jenis = binding.etJenis.text.toString()
        val guides = binding.etGuides.text.toString()
        val handle = binding.etHandle.text.toString()
        // ▲▲▲ SELESAI MELENGKAPI VARIABEL ▲▲▲

        val photo = imagePath ?: ""
        val category = binding.etCategory.text.toString()

        val newJoran = Joran(
            id = joran?.id ?: 0,
            name = name,
            price = price,
            description = description,
            photo = photo,
            panjang = panjang,
            power = power,
            material = material,
            aksi = aksi,
            jenis = jenis,
            guides = guides,
            handle = handle,
            category = category,
            stok = stok
        )

        if (joran != null) {
            dbHelper.updateJoran(newJoran)
        } else {
            dbHelper.addJoran(newJoran)
        }

        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun saveImageToInternalStorage(uri: Uri): String? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            val fileName = "joran_${System.currentTimeMillis()}.jpg"
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
