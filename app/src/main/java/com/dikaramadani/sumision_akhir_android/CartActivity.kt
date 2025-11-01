package com.dikaramadani.sumision_akhir_android

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dikaramadani.sumision_akhir_android.databinding.ActivityCartBinding
import java.text.NumberFormat
import java.util.Locale

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var cartAdapter: CartAdapter
    private var cartItems = mutableListOf<CartItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarCart)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        dbHelper = DatabaseHelper(this)

        setupRecyclerView()

        binding.btnCheckout.setOnClickListener {
            if (cartItems.isNotEmpty()) {
                showCheckoutConfirmationDialog()
            } else {
                Toast.makeText(this, "Keranjang Anda kosong.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadCartItems()
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(cartItems) { clickedItem ->
            showDeleteConfirmationDialog(clickedItem)
        }
        binding.rvCartItems.layoutManager = LinearLayoutManager(this)
        binding.rvCartItems.adapter = cartAdapter
    }

    private fun loadCartItems() {
        val userId = 1
        val itemsFromDb = dbHelper.getCartItems(userId)

        cartItems.clear()
        cartItems.addAll(itemsFromDb)

        if (cartItems.isEmpty()) {
            binding.rvCartItems.visibility = View.GONE
            binding.tvEmptyCart.visibility = View.VISIBLE
            binding.bottomBar.visibility = View.GONE
        } else {
            binding.rvCartItems.visibility = View.VISIBLE
            binding.tvEmptyCart.visibility = View.GONE
            binding.bottomBar.visibility = View.VISIBLE
        }

        cartAdapter.notifyDataSetChanged()
        calculateTotalPrice()
    }

    private fun calculateTotalPrice() {
        var totalPrice = 0.0
        for (item in cartItems) {
            val priceString = item.joranPrice
                ?.replace("Rp", "")
                ?.replace(".", "")
                ?.trim()

            val price = priceString?.toDoubleOrNull() ?: 0.0
            totalPrice += price * item.jumlah
        }

        val localeID = Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(localeID)
        numberFormat.maximumFractionDigits = 0
        binding.tvTotalPrice.text = numberFormat.format(totalPrice)
    }

    private fun showDeleteConfirmationDialog(cartItem: CartItem) {
        AlertDialog.Builder(this)
            .setTitle("Hapus Item")
            .setMessage("Anda yakin ingin menghapus ${cartItem.joranName} dari keranjang?")
            .setPositiveButton("Hapus") { _, _ ->
                dbHelper.deleteCartItem(cartItem.id)
                Toast.makeText(this, "${cartItem.joranName} dihapus", Toast.LENGTH_SHORT).show()
                loadCartItems()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showCheckoutConfirmationDialog() {
        val totalItems = cartItems.sumOf { it.jumlah }
        val totalPrice = binding.tvTotalPrice.text.toString()

        val message = "Apakah Anda yakin ingin membeli $totalItems barang dengan total harga $totalPrice?"

        AlertDialog.Builder(this)
            .setTitle("Konfirmasi Checkout")
            .setMessage(message)
            .setPositiveButton("Ya, Bayar") { _, _ ->
                val userId = 1
                val success = dbHelper.checkout(userId, cartItems, totalPrice)

                if (success) {
                    Toast.makeText(this, "Checkout berhasil! Terima kasih.", Toast.LENGTH_LONG).show()
                    setResult(Activity.RESULT_OK) // <-- KIRIM SINYAL SUKSES
                    finish()
                } else {
                    Toast.makeText(this, "Checkout gagal. Stok barang mungkin telah berubah.", Toast.LENGTH_LONG).show()
                    loadCartItems()
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
