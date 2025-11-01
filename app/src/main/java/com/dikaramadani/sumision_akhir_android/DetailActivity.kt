package com.dikaramadani.sumision_akhir_android

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.dikaramadani.sumision_akhir_android.databinding.ActivityDetailBinding
import java.io.File

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_JORAN = "extra_joran"
    }

    private lateinit var binding: ActivityDetailBinding
    private lateinit var dbHelper: DatabaseHelper
    private var currentJoran: Joran? = null

    private val cartLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            currentJoran?.let {
                val updatedJoran = dbHelper.getJoranById(it.id)
                if (updatedJoran != null) {
                    currentJoran = updatedJoran
                    populateUi(updatedJoran)
                } else {
                    Toast.makeText(this, "Joran tidak lagi tersedia.", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        dbHelper = DatabaseHelper(this)

        val joranFromIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_JORAN, Joran::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_JORAN)
        }

        if (joranFromIntent != null) {
            currentJoran = joranFromIntent
            populateUi(joranFromIntent)
            setupAddToCartButton(joranFromIntent)
        } else {
            Toast.makeText(this, "Gagal memuat joran.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun populateUi(joran: Joran) {
        binding.collapsingToolbar.title = joran.name
        binding.tvDetailName.text = joran.name
        binding.tvDetailPrice.text = joran.price
        binding.tvDetailDescription.text = joran.description
        binding.tvDetailStok.text = "Stok: ${joran.stok}"

        if (joran.photo.isNotEmpty()) {
            binding.imgDetailPhoto.setImageURI(Uri.fromFile(File(joran.photo)))
        } else {
            binding.imgDetailPhoto.setImageResource(R.drawable.ic_launcher_background)
        }

        binding.tableSpecifications.removeAllViews()
        addSpecRow("Kategori", joran.category)
        addSpecRow("Panjang", joran.panjang)
        addSpecRow("Power", joran.power)
        addSpecRow("Material", joran.material)
        addSpecRow("Aksi", joran.aksi)
        addSpecRow("Jenis", joran.jenis)
        addSpecRow("Guides", joran.guides)
        addSpecRow("Handle", joran.handle)

        if (joran.stok <= 0) {
            binding.tvDetailStok.text = "Stok habis"
            binding.etQuantity.isEnabled = false
            binding.btnAddToCart.isEnabled = false
            binding.btnAddToCart.text = "Stok Habis"
        } else {
            binding.etQuantity.isEnabled = true
            binding.btnAddToCart.isEnabled = true
            binding.btnAddToCart.text = "Tambah ke Keranjang"
        }
    }

    private fun addSpecRow(label: String, value: String?) {
        if (value.isNullOrEmpty()) return

        val tableRow = TableRow(this)
        tableRow.layoutParams = TableLayout.LayoutParams(
            TableLayout.LayoutParams.MATCH_PARENT,
            TableLayout.LayoutParams.WRAP_CONTENT
        )

        val labelTextView = TextView(this).apply {
            text = label
            layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.0f)
            setTextAppearance(androidx.appcompat.R.style.TextAppearance_AppCompat_Body1)
        }

        val valueTextView = TextView(this).apply {
            text = value
            layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT)
            gravity = Gravity.END
            setTextAppearance(androidx.appcompat.R.style.TextAppearance_AppCompat_Body2)
        }

        tableRow.addView(labelTextView)
        tableRow.addView(valueTextView)
        binding.tableSpecifications.addView(tableRow)
    }

    private fun setupAddToCartButton(joran: Joran) {
        binding.btnAddToCart.setOnClickListener {
            val quantityString = binding.etQuantity.text.toString()
            if (quantityString.isEmpty()) {
                Toast.makeText(this, "Masukkan jumlah barang", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val quantityToAdd = quantityString.toInt()
            if (quantityToAdd <= 0) {
                Toast.makeText(this, "Jumlah harus lebih dari 0", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userId = 1
            val quantityInCart = dbHelper.getQuantityInCart(userId, joran.id)
            val totalQuantity = quantityInCart + quantityToAdd

            if (totalQuantity > joran.stok) {
                val remainingStock = joran.stok - quantityInCart
                val message = if (remainingStock > 0) {
                    "Jumlah melebihi stok. Anda sudah punya $quantityInCart di keranjang. Anda hanya bisa menambahkan $remainingStock item lagi."
                } else {
                    "Jumlah melebihi stok. Semua stok untuk item ini sudah ada di keranjang Anda."
                }
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            dbHelper.addToCart(userId, joran.id, quantityToAdd)
            Toast.makeText(this, "${joran.name} (x$quantityToAdd) ditambahkan ke keranjang", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_share -> {
                val shareText = "Lihat joran ini: ${currentJoran?.name}\n\n${currentJoran?.description}"
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, shareText)
                }
                startActivity(Intent.createChooser(shareIntent, "Bagikan melalui"))
                true
            }
            R.id.action_cart_detail -> { 
                val intent = Intent(this, CartActivity::class.java)
                cartLauncher.launch(intent)
                true
            }
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
