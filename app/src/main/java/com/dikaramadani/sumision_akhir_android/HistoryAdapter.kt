package com.dikaramadani.sumision_akhir_android

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dikaramadani.sumision_akhir_android.databinding.ItemHistoryBinding

class HistoryAdapter(
    private var transactions: List<Transaction>
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.binding.apply {
            tvHistoryDate.text = transaction.transactionDate
            tvHistoryTotal.text = transaction.totalPrice
            tvHistoryDetails.text = transaction.details
        }
    }

    override fun getItemCount(): Int = transactions.size
}
    