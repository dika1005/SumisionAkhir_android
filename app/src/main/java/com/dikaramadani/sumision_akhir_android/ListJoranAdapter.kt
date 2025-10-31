package com.dikaramadani.sumision_akhir_android

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class ListJoranAdapter(
    private val listJoran: ArrayList<Joran>,
    private val isAdmin: Boolean,
    private val onItemClick: (Joran) -> Unit,
    private val onEdit: (Joran) -> Unit,
    private val onDelete: (Joran) -> Unit
) : RecyclerView.Adapter<ListJoranAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_joran, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val joran = listJoran[position]
        if (joran.photo.isNotEmpty()) {
            holder.imgPhoto.setImageURI(Uri.fromFile(File(joran.photo)))
        } else {
            holder.imgPhoto.setImageResource(R.drawable.ic_launcher_background)
        }
        holder.tvName.text = joran.name
        holder.tvDescription.text = joran.description
        holder.itemView.setOnClickListener {
            onItemClick(joran)
        }
        holder.itemView.setOnLongClickListener {
            showPopupMenu(holder.itemView, joran)
            true
        }
    }

    private fun showPopupMenu(view: View, joran: Joran) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.inflate(R.menu.menu_item)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_edit -> {
                    onEdit(joran)
                    true
                }
                R.id.action_delete_item -> {
                    AlertDialog.Builder(view.context)
                        .setTitle("Hapus Joran")
                        .setMessage("Apakah Anda yakin ingin menghapus item ini?")
                        .setPositiveButton("Ya") { _, _ -> onDelete(joran) }
                        .setNegativeButton("Tidak", null)
                        .show()
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    override fun getItemCount(): Int = listJoran.size

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPhoto: ImageView = itemView.findViewById(R.id.img_item_photo)
        val tvName: TextView = itemView.findViewById(R.id.tv_item_name)
        val tvDescription: TextView = itemView.findViewById(R.id.tv_item_description)
    }
}