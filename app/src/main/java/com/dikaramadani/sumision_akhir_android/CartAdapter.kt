package com.dikaramadani.sumision_akhir_android

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dikaramadani.sumision_akhir_android.databinding.ItemCartBinding // Menggunakan View Binding
import java.io.File

// Adapter ini menerima list CartItem dan sebuah fungsi 'lambda' untuk menangani klik hapus
class CartAdapter(
    private var cartItems: MutableList<CartItem>,
    private val onDeleteClick: (CartItem) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    // ViewHolder berisi referensi ke semua view di dalam item_cart.xml
    inner class CartViewHolder(val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        // Membuat ViewHolder baru dengan layout item_cart.xml
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val currentItem = cartItems[position]

        // Mengisi data ke dalam view menggunakan binding
        holder.binding.apply {
            tvCartItemName.text = currentItem.joranName
            tvCartItemPrice.text = currentItem.joranPrice
            tvCartItemQuantity.text = "Jumlah: ${currentItem.jumlah}"

            // Menampilkan gambar
            if (!currentItem.joranPhoto.isNullOrEmpty()) {
                ivCartItemPhoto.setImageURI(Uri.fromFile(File(currentItem.joranPhoto!!)))
            } else {
                ivCartItemPhoto.setImageResource(R.drawable.ic_launcher_background) // Gambar default
            }

            // Memberikan aksi ke tombol hapus
            btnDeleteItem.setOnClickListener {
                onDeleteClick(currentItem)
            }
        }
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }
}
