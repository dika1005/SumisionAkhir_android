package com.dikaramadani.sumision_akhir_android

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dikaramadani.sumision_akhir_android.databinding.ActivityMainBinding
import com.google.android.material.chip.Chip

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var listJoranAdapter: ListJoranAdapter
    private val listJoran = ArrayList<Joran>()
    private var isAdmin: Boolean = false
    private var selectedCategory: String? = "Semua"

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // Cukup onResume() yang akan menangani pembaruan data
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userRole = intent.getStringExtra("USER_ROLE")
        isAdmin = userRole == "admin"

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Koleksi Joran"

        dbHelper = DatabaseHelper(this)

        setupUI()
    }

    override fun onResume() {
        super.onResume()
        // Muat ulang data setiap kali activity kembali aktif
        loadJoran(selectedCategory)
    }

    private fun setupUI() {
        if (isAdmin) {
            binding.fabAdd.visibility = View.VISIBLE
        } else {
            binding.fabAdd.visibility = View.GONE
        }

        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddEditActivity::class.java)
            resultLauncher.launch(intent)
        }

        binding.rvJoran.layoutManager = LinearLayoutManager(this)

        listJoranAdapter = ListJoranAdapter(
            listJoran = listJoran,
            isAdmin = isAdmin,
            onItemClick = { joran ->
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_JORAN, joran)
                resultLauncher.launch(intent) // Gunakan launcher agar bisa refresh saat kembali
            },
            onEdit = { joran ->
                val intent = Intent(this@MainActivity, AddEditActivity::class.java)
                intent.putExtra(AddEditActivity.EXTRA_JORAN, joran)
                resultLauncher.launch(intent)
            },
            onDelete = { joran ->
                dbHelper.deleteJoran(joran)
                loadJoran(selectedCategory)
            }
        )
        binding.rvJoran.adapter = listJoranAdapter

        setupCategoryChips()
    }

    private fun setupCategoryChips() {
        binding.categoryChipGroup.setOnCheckedChangeListener { group, checkedId ->
            val chip: Chip? = group.findViewById(checkedId)
            selectedCategory = chip?.text.toString()
            loadJoran(selectedCategory)
        }
    }

    private fun loadJoran(category: String?) {
        listJoran.clear()
        val newList = dbHelper.getAllJoran(category)
        listJoran.addAll(newList)
        listJoranAdapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_cart -> {
                val intent = Intent(this, CartActivity::class.java)
                resultLauncher.launch(intent)
                return true
            }
            R.id.action_profile -> {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_logout -> {
                val sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.clear()
                editor.apply()

                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
