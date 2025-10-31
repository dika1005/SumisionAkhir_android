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

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var listJoranAdapter: ListJoranAdapter
    private val listJoran = ArrayList<Joran>()
    private var isAdmin: Boolean = false

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            loadJoran() // Muat ulang data jika Add/Edit/Delete berhasil
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

        // PERUBAHAN: Atur visibilitas tombol FAB berdasarkan role
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

        // PERUBAHAN: Teruskan status admin ke adapter
        listJoranAdapter = ListJoranAdapter(
            listJoran = listJoran,   // Beri nama untuk argumen pertama
            isAdmin = isAdmin,       // Beri nama untuk argumen kedua
            onItemClick = { joran ->
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_JORAN, joran)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                startActivity(intent)
            },
            onEdit = { joran ->
                val intent = Intent(this@MainActivity, AddEditActivity::class.java)
                intent.putExtra(AddEditActivity.EXTRA_JORAN, joran)
                resultLauncher.launch(intent)
            },
            onDelete = { joran ->
                dbHelper.deleteJoran(joran)
                loadJoran()
            }
        )
        binding.rvJoran.adapter = listJoranAdapter

        loadJoran()
    }


    private fun loadJoran() {
        listJoran.clear()
        val cursor = dbHelper.getAllJoran()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME))
            val price = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRICE))
            val description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION))
            val photo = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PHOTO))
            val panjang = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PANJANG))
            val power = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_POWER))
            val material = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MATERIAL))
            val aksi = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AKSI))
            val jenis = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_JENIS))
            val guides = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_GUIDES))
            val handle = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_HANDLE))

            listJoran.add(Joran(id, name, price, description, photo, panjang, power, material, aksi, jenis, guides, handle))
        }
        cursor.close()
        listJoranAdapter.notifyDataSetChanged()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Pastikan nama file menu Anda adalah "menu_main.xml"
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // Fungsi untuk memberi aksi saat item menu diklik
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // Jika item "Profile" diklik
            R.id.action_profile -> {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
                return true
            }

            // Jika item "Logout" diklik
            R.id.action_logout -> {
                // Logika Logout
                val sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.clear()
                editor.apply()

                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
                // Selesai Logika Logout
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
