package com.dikaramadani.sumision_akhir_android

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import java.io.File

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_JORAN = "extra_joran"
    }

    private var joran: Joran? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val toolbar: Toolbar = findViewById(R.id.toolbar_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        joran = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_JORAN, Joran::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<Joran>(EXTRA_JORAN)
        }

        if (joran != null) {
            supportActionBar?.title = joran!!.name
            val imgDetailPhoto = findViewById<ImageView>(R.id.img_detail_photo)
            if (joran!!.photo.isNotEmpty()) {
                imgDetailPhoto.setImageURI(Uri.fromFile(File(joran!!.photo)))
            } else {
                imgDetailPhoto.setImageResource(R.drawable.ic_launcher_background)
            }
            findViewById<TextView>(R.id.tv_detail_name).text = joran!!.name
            findViewById<TextView>(R.id.tv_detail_price).text = joran!!.price
            findViewById<TextView>(R.id.tv_detail_description).text = joran!!.description

            // Spesifikasi
            setupSpecificationRow(findViewById(R.id.row_panjang), findViewById(R.id.tv_detail_panjang), joran!!.panjang)
            setupSpecificationRow(findViewById(R.id.row_power), findViewById(R.id.tv_detail_power), joran!!.power)
            setupSpecificationRow(findViewById(R.id.row_material), findViewById(R.id.tv_detail_material), joran!!.material)
            setupSpecificationRow(findViewById(R.id.row_aksi), findViewById(R.id.tv_detail_aksi), joran!!.aksi)
            setupSpecificationRow(findViewById(R.id.row_jenis), findViewById(R.id.tv_detail_jenis), joran!!.jenis)
            setupSpecificationRow(findViewById(R.id.row_guides), findViewById(R.id.tv_detail_guides), joran!!.guides)
            setupSpecificationRow(findViewById(R.id.row_handle), findViewById(R.id.tv_detail_handle), joran!!.handle)
        }

        val btnBuy: Button = findViewById(R.id.btn_buy)
        btnBuy.setOnClickListener {
            Toast.makeText(this, "Anda telah membeli ${joran?.name}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_share) {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            val shareText = "Lihat joran ini: ${joran?.name}\n\n${joran?.description}"
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText)
            startActivity(Intent.createChooser(shareIntent, "Bagikan melalui"))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupSpecificationRow(row: TableRow, textView: TextView, data: String?) {
        if (!data.isNullOrEmpty()) {
            textView.text = data
        } else {
            row.visibility = View.GONE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}