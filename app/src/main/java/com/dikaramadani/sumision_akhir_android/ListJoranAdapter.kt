package com.dikaramadani.sumision_akhir_android

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.dikaramadani.sumision_akhir_android.databinding.ItemRowJoranBinding
import java.io.File

class ListJoranAdapter(
    private val listJoran: ArrayList<Joran>,
    private val isAdmin: Boolean,
    private val onItemClick: (Joran) -> Unit,
    private val onEdit: (Joran) -> Unit,
    private val onDelete: (Joran) -> Unit
) : RecyclerView.Adapter<ListJoranAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowJoranBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val joran = listJoran[position]
        holder.bind(joran)
    }

    override fun getItemCount(): Int = listJoran.size

    inner class ListViewHolder(private val binding: ItemRowJoranBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(joran: Joran) {
            binding.tvItemName.text = joran.name
            binding.tvItemPrice.text = joran.price
            binding.tvItemStock.text = "Stok: ${joran.stok}"

            if (joran.photo.isNotEmpty()) {
                binding.ivItemPhoto.setImageURI(Uri.fromFile(File(joran.photo)))
            } else {
                binding.ivItemPhoto.setImageResource(R.drawable.ic_launcher_background) // Gambar default
            }

            itemView.setOnClickListener {
                onItemClick(joran)
            }

            if (isAdmin) {
                binding.btnEdit.visibility = View.VISIBLE
                binding.btnDelete.visibility = View.VISIBLE

                binding.btnEdit.setOnClickListener {
                    onEdit(joran)
                }

                binding.btnDelete.setOnClickListener {
                    AlertDialog.Builder(itemView.context)
                        .setTitle("Hapus Joran")
                        .setMessage("Apakah Anda yakin ingin menghapus ${joran.name}?")
                        .setPositiveButton("Ya") { _, _ -> onDelete(joran) }
                        .setNegativeButton("Tidak", null)
                        .show()
                }
            } else {
                binding.btnEdit.visibility = View.GONE
                binding.btnDelete.visibility = View.GONE
            }
        }
    }
}