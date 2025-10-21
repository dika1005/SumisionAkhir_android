package com.dikaramadani.sumision_akhir_android

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var rvJoran: RecyclerView
    private val list = ArrayList<Joran>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        rvJoran = findViewById(R.id.rv_joran)
        rvJoran.setHasFixedSize(true)

        list.addAll(getListJoran())
        showRecyclerList()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_profile -> {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getListJoran(): ArrayList<Joran> {
        val listJoran = ArrayList<Joran>()
        listJoran.add(Joran(
            name = "Shimano Majestic",
            price = "±Rp 639.000",
            description = "Joran all-round yang ringan dan kuat, cocok untuk pemancing menengah. Dirancang agar fleksibel untuk berbagai kondisi air tawar maupun laut ringan. Sensitivitas tinggi dengan kekuatan yang cukup solid.",
            photo = R.drawable.majestic,
            panjang = "1.93 – 2.29 m",
            power = "Medium",
            material = "High Carbon",
            aksi = "Fast",
            jenis = "Spinning / Casting"
        ))
        listJoran.add(Joran(
            name = "Shimano Majestic XT",
            price = "±Rp 950.000",
            description = "Versi upgrade dari seri Majestic biasa. Dilengkapi komponen Fuji dan handle ergonomis, memberikan performa lebih baik untuk teknik casting. Cocok buat pemancing yang mulai serius.",
            photo = R.drawable.majestic_xt,
            panjang = "1.63 – 2.49 m",
            power = "Medium",
            material = "90% Carbon",
            guides = "Fuji O-Ring",
            jenis = "Spinning / Casting"
        ))
        listJoran.add(Joran(
            name = "Shimano Catana",
            price = "±Rp 1.690.000",
            description = "Seri populer yang dikenal ringan, kuat, dan sensitif. Ideal untuk pemula hingga menengah yang ingin joran berkualitas dengan harga terjangkau. Performa seimbang di air tawar maupun payau.",
            photo = R.drawable.catana,
            panjang = "2.13 m (7’0\")",
            power = "Medium-Fast",
            material = "24-Ton Toray Graphite",
            handle = "EVA + Cork",
            jenis = "Spinning"
        ))
        listJoran.add(Joran(
            name = "Shimano Air Swing",
            price = "±Rp 1.485.000",
            description = "Joran ringan untuk teknik lure kecil atau ultralight fishing. Mudah dikontrol, cocok untuk mancing di sungai atau waduk dangkal dengan ikan kecil-menengah.",
            photo = R.drawable.air_swing,
            panjang = "6’0\" – 7’0\"",
            power = "Light – Medium",
            material = "Carbon Composite",
            handle = "EVA Grip",
            jenis = "Spinning"
        ))
        listJoran.add(Joran(
            name = "Shimano Air Swing 7'0\"",
            price = "±Rp 1.672.000",
            description = "Didesain untuk lemparan jarak jauh. Cocok untuk mancing di pantai atau spot terbuka. Ringan tapi tetap stabil saat fight ikan ukuran sedang.",
            photo = R.drawable.air_swing_7,
            panjang = "7’0\" (2.13 m)",
            power = "Medium",
            material = "Carbon",
            aksi = "Fast",
            jenis = "Casting"
        ))
        listJoran.add(Joran(
            name = "Shimano Bassterra GS",
            price = "±Rp 2.330.000",
            description = "Dirancang khusus untuk kompetisi galatama, memberikan kecepatan dan sensitivitas tinggi. Material premium membuatnya kuat dan responsif terhadap tarikan ikan besar.",
            photo = R.drawable.bassterra,
            panjang = "1.68 m (5’6\")",
            power = "Medium",
            material = "High Modulus Carbon",
            guides = "Fuji Alconite",
            jenis = "Galatama / Kolam Lomba"
        ))
        listJoran.add(Joran(
            name = "Shimano Lesath GS",
            price = "±Rp 5.367.000",
            description = "Seri premium untuk pemancing kompetitif. Menggabungkan teknologi karbon tingkat tinggi dengan desain elegan. Sensitivitas dan daya tahan luar biasa, cocok untuk lomba profesional.",
            photo = R.drawable.lesath,
            panjang = "2.8 – 2.9 m",
            power = "Medium-Heavy",
            material = "Full Carbon (Spiral X, Hi-Power X)",
            guides = "Fuji SiC",
            jenis = "Galatama / Lomba"
        ))
        listJoran.add(Joran(
            name = "Shimano Grappler Type J",
            price = "±Rp 2.335.000",
            description = "Joran kuat untuk teknik jigging laut dalam. Memiliki tenaga besar untuk menghadapi ikan besar dan tarikan berat. Cocok untuk pemancing berpengalaman yang suka tantangan ekstrem.",
            photo = R.drawable.grappler,
            panjang = "1.68 m (5’6\")",
            power = "Extra Heavy",
            material = "Hi-Power X Carbon",
            aksi = "Fast",
            jenis = "Jigging (Laut)"
        ))
        listJoran.add(Joran(
            name = "Shimano Zodias",
            price = "", // Harga tidak disebutkan
            description = "Joran premium untuk pemancing yang menginginkan keseimbangan sempurna antara kekuatan dan sensitivitas. Ideal buat casting ikan predator air tawar.",
            photo = R.drawable.zodias,
            panjang = "6’10\" – 7’2\"",
            power = "Medium – Medium Heavy",
            material = "Carbon Hi-Power X",
            aksi = "Fast",
            handle = "CI4+" // Reel seat sebagai handle
        ))
        listJoran.add(Joran(
            name = "Shimano Poison Adrena",
            price = "", // Harga tidak disebutkan
            description = "Joran kelas atas hasil kolaborasi Shimano dan Jackall, terkenal dengan getaran sensitif dan kontrol luar biasa untuk teknik finesse maupun heavy lure.",
            photo = R.drawable.poison_adrena,
            panjang = "6’8\" – 7’4\"",
            power = "Medium – Heavy",
            material = "Spiral X Core Carbon",
            aksi = "Extra Fast",
            handle = "Carbon Monocoque Grip"
        ))
        return listJoran
    }

    private fun showRecyclerList() {
        rvJoran.layoutManager = LinearLayoutManager(this)
        val listJoranAdapter = ListJoranAdapter(list)
        rvJoran.adapter = listJoranAdapter
    }
}