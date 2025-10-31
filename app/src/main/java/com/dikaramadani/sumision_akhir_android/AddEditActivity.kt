package com.dikaramadani.sumision_akhir_android

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class AddEditActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etPrice: EditText
    private lateinit var etDescription: EditText
    private lateinit var etPanjang: EditText
    private lateinit var etPower: EditText
    private lateinit var etMaterial: EditText
    private lateinit var etAksi: EditText
    private lateinit var etJenis: EditText
    private lateinit var etGuides: EditText
    private lateinit var etHandle: EditText
    private lateinit var btnSubmit: Button

    private var isEdit = false
    private var joran: Joran? = null
    private lateinit var dbHelper: DatabaseHelper

    companion object {
        const val EXTRA_JORAN = "extra_joran"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit)

        etName = findViewById(R.id.et_name)
        etPrice = findViewById(R.id.et_price)
        etDescription = findViewById(R.id.et_description)
        etPanjang = findViewById(R.id.et_panjang)
        etPower = findViewById(R.id.et_power)
        etMaterial = findViewById(R.id.et_material)
        etAksi = findViewById(R.id.et_aksi)
        etJenis = findViewById(R.id.et_jenis)
        etGuides = findViewById(R.id.et_guides)
        etHandle = findViewById(R.id.et_handle)
        btnSubmit = findViewById(R.id.btn_submit)

        dbHelper = DatabaseHelper(this)

        joran = intent.getParcelableExtra(EXTRA_JORAN)
        if (joran != null) {
            isEdit = true
            btnSubmit.text = "Update"
            joran?.let { 
                etName.setText(it.name)
                etPrice.setText(it.price)
                etDescription.setText(it.description)
                etPanjang.setText(it.panjang)
                etPower.setText(it.power)
                etMaterial.setText(it.material)
                etAksi.setText(it.aksi)
                etJenis.setText(it.jenis)
                etGuides.setText(it.guides)
                etHandle.setText(it.handle)
            }
        } else {
            btnSubmit.text = "Submit"
        }

        supportActionBar?.title = if (isEdit) "Edit Joran" else "Add Joran"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btnSubmit.setOnClickListener {
            submit()
        }
    }

    private fun submit() {
        val name = etName.text.toString().trim()
        val price = etPrice.text.toString().trim()
        val description = etDescription.text.toString().trim()
        val panjang = etPanjang.text.toString().trim()
        val power = etPower.text.toString().trim()
        val material = etMaterial.text.toString().trim()
        val aksi = etAksi.text.toString().trim()
        val jenis = etJenis.text.toString().trim()
        val guides = etGuides.text.toString().trim()
        val handle = etHandle.text.toString().trim()

        if (name.isEmpty()) {
            etName.error = "Field can not be blank"
            return
        }

        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_NAME, name)
            put(DatabaseHelper.COLUMN_PRICE, price)
            put(DatabaseHelper.COLUMN_DESCRIPTION, description)
            put(DatabaseHelper.COLUMN_PANJANG, panjang)
            put(DatabaseHelper.COLUMN_POWER, power)
            put(DatabaseHelper.COLUMN_MATERIAL, material)
            put(DatabaseHelper.COLUMN_AKSI, aksi)
            put(DatabaseHelper.COLUMN_JENIS, jenis)
            put(DatabaseHelper.COLUMN_GUIDES, guides)
            put(DatabaseHelper.COLUMN_HANDLE, handle)
        }

        if (isEdit) {
            val db = dbHelper.writableDatabase
            val result = db.update(DatabaseHelper.TABLE_NAME, values, "${DatabaseHelper.COLUMN_ID} = ?", arrayOf(joran?.id.toString()))
            if (result > 0) {
                setResult(MainActivity.RESULT_UPDATE)
                finish()
            } else {
                Toast.makeText(this, "Failed to update data", Toast.LENGTH_SHORT).show()
            }
        } else {
            val db = dbHelper.writableDatabase
            val result = db.insert(DatabaseHelper.TABLE_NAME, null, values)
            if (result > 0) {
                val newJoran = Joran(id = result.toInt(), name = name, price = price, description = description, photo = R.drawable.ic_launcher_background, panjang = panjang, power = power, material = material, aksi = aksi, jenis = jenis, guides = guides, handle = handle)
                val intent = Intent()
                intent.putExtra(EXTRA_JORAN, newJoran)
                setResult(MainActivity.RESULT_ADD, intent)
                finish()
            } else {
                Toast.makeText(this, "Failed to add data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isEdit) {
            menuInflater.inflate(R.menu.menu_form, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> {
                showDeleteConfirmationDialog()
                true
            }
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showDeleteConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Joran")
        builder.setMessage("Are you sure you want to delete this joran?")
        builder.setPositiveButton("Yes") { _, _ ->
            deleteJoran()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.cancel()
        }
        builder.show()
    }

    private fun deleteJoran() {
        val db = dbHelper.writableDatabase
        val result = db.delete(DatabaseHelper.TABLE_NAME, "${DatabaseHelper.COLUMN_ID} = ?", arrayOf(joran?.id.toString()))
        if (result > 0) {
            setResult(MainActivity.RESULT_DELETE)
            finish()
        } else {
            Toast.makeText(this, "Failed to delete data", Toast.LENGTH_SHORT).show()
        }
    }
}