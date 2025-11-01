package com.dikaramadani.sumision_akhir_android

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dikaramadani.sumision_akhir_android.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var historyAdapter: HistoryAdapter
    private var transactionList = mutableListOf<Transaction>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarHistory)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        dbHelper = DatabaseHelper(this)

        setupRecyclerView()
        loadHistory()
    }

    private fun setupRecyclerView() {
        historyAdapter = HistoryAdapter(transactionList)
        binding.rvHistory.layoutManager = LinearLayoutManager(this)
        binding.rvHistory.adapter = historyAdapter
    }

    private fun loadHistory() {
        // Anggap user ID yang login adalah 1
        val userId = 1
        val historyFromDb = dbHelper.getTransactionHistory(userId)

        transactionList.clear()
        transactionList.addAll(historyFromDb)

        if (transactionList.isEmpty()) {
            binding.rvHistory.visibility = View.GONE
            binding.tvEmptyHistory.visibility = View.VISIBLE
        } else {
            binding.rvHistory.visibility = View.VISIBLE
            binding.tvEmptyHistory.visibility = View.GONE
        }

        historyAdapter.notifyDataSetChanged()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
    