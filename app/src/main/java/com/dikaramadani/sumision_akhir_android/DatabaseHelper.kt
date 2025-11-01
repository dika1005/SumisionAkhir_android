package com.dikaramadani.sumision_akhir_android

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "joran.db"
        private const val DATABASE_VERSION = 10

        const val TABLE_JORAN = "joran"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_PRICE = "price"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_PHOTO = "photo"
        const val COLUMN_PANJANG = "panjang"
        const val COLUMN_POWER = "power"
        const val COLUMN_MATERIAL = "material"
        const val COLUMN_AKSI = "aksi"
        const val COLUMN_JENIS = "jenis"
        const val COLUMN_GUIDES = "guides"
        const val COLUMN_HANDLE = "handle"
        const val COLUMN_JORAN_CATEGORY = "category"
        const val COLUMN_JORAN_STOK = "stok"

        const val TABLE_USERS = "users"
        const val COLUMN_USER_ID = "id"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_USER_ALAMAT = "alamat"
        const val COLUMN_USER_ROLE = "role"
        const val COLUMN_USER_PHOTO = "photo"

        const val TABLE_CART = "cart"
        const val COLUMN_CART_ID = "id"
        const val COLUMN_CART_USER_ID = "user_id"
        const val COLUMN_CART_JORAN_ID = "joran_id"
        const val COLUMN_CART_JUMLAH = "jumlah"

        const val TABLE_TRANSACTIONS = "transactions"
        const val COLUMN_TRANS_ID = "id"
        const val COLUMN_TRANS_USER_ID = "user_id"
        const val COLUMN_TRANS_DETAILS = "details"
        const val COLUMN_TRANS_TOTAL_PRICE = "total_price"
        const val COLUMN_TRANS_DATE = "transaction_date"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createJoranTable = "CREATE TABLE $TABLE_JORAN (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NAME TEXT, $COLUMN_PRICE TEXT, $COLUMN_DESCRIPTION TEXT, " +
                "$COLUMN_PHOTO TEXT, $COLUMN_PANJANG TEXT, $COLUMN_POWER TEXT, " +
                "$COLUMN_MATERIAL TEXT, $COLUMN_AKSI TEXT, $COLUMN_JENIS TEXT, " +
                "$COLUMN_GUIDES TEXT, $COLUMN_HANDLE TEXT, $COLUMN_JORAN_CATEGORY TEXT, " +
                "$COLUMN_JORAN_STOK INTEGER DEFAULT 0)"
        db.execSQL(createJoranTable)

        val createUserTable = "CREATE TABLE $TABLE_USERS (" +
                "$COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_USERNAME TEXT, $COLUMN_PASSWORD TEXT, $COLUMN_EMAIL TEXT, " +
                "$COLUMN_USER_ALAMAT TEXT, $COLUMN_USER_ROLE TEXT, $COLUMN_USER_PHOTO TEXT)"
        db.execSQL(createUserTable)

        val createCartTable = "CREATE TABLE $TABLE_CART (" +
                "$COLUMN_CART_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_CART_USER_ID INTEGER, $COLUMN_CART_JORAN_ID INTEGER, " +
                "$COLUMN_CART_JUMLAH INTEGER)"
        db.execSQL(createCartTable)

        val createTransactionsTable = "CREATE TABLE $TABLE_TRANSACTIONS (" +
                "$COLUMN_TRANS_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_TRANS_USER_ID INTEGER, $COLUMN_TRANS_DETAILS TEXT, " +
                "$COLUMN_TRANS_TOTAL_PRICE TEXT, $COLUMN_TRANS_DATE TEXT)"
        db.execSQL(createTransactionsTable)

        // --- SEEDING DATA --- //
        seedData(db)
    }

    private fun seedData(db: SQLiteDatabase) {
        // Buat Akun Admin
        val adminValues = ContentValues().apply {
            put(COLUMN_USERNAME, "Admin Dika")
            put(COLUMN_PASSWORD, "admin123")
            put(COLUMN_EMAIL, "admin@gmail.com")
            put(COLUMN_USER_ALAMAT, "Kantor Pusat")
            put(COLUMN_USER_ROLE, "admin")
            put(COLUMN_USER_PHOTO, "")
        }
        db.insert(TABLE_USERS, null, adminValues)

        // Daftar Joran untuk di-seed
        val joransToSeed = listOf(
            // Laut
            Joran(0, "Shimano Grappler Type J", "Rp 2.800.000", "Joran jigging legendaris untuk perairan dalam, ringan dan sangat kuat.", "", "1.91m", "Medium", "Spiral X Core", "Regular Fast", "Spinning", "Fuji SiC", "EVA", "Laut", 12),
            Joran(0, "Daiwa Saltiga G", "Rp 4.500.000", "Joran popping premium untuk target GT besar. Kekuatan tanpa kompromi.", "", "2.4m", "Heavy", "HVF Nanoplus", "Fast", "Spinning", "Fuji K-Series SiC", "EVA", "Laut", 8),

            // Danau
            Joran(0, "Storm Adventure Xtreme", "Rp 750.000", "Joran casting serbaguna, cocok untuk berbagai jenis lure di danau.", "", "1.98m", "Medium", "Graphite", "Fast", "Casting", "Fuji O-Ring", "Cork", "Danau", 25),
            Joran(0, "Major Craft Benkei", "Rp 1.300.000", "Joran bass fishing dari Jepang, sangat sensitif dan ringan.", "", "1.95m", "Medium-Light", "High-Modulus Carbon", "Extra Fast", "Spinning", "Fuji Alconite K-Series", "Split EVA", "Danau", 18),

            // Sungai
            Joran(0, "Relix Nusantara Capung 702UL", "Rp 400.000", "Joran ultralight populer untuk mancing di sungai kecil (stream).", "", "2.1m", "Ultra-Light", "Carbon", "Fast", "Spinning", "Seaguide", "Full Cork", "Sungai", 45),
            Joran(0, "Maguro Extreme Cast", "Rp 650.000", "Joran baitcasting yang kuat dan akurat untuk menyusuri pinggiran sungai.", "", "1.8m", "Medium", "24T Carbon", "Fast", "Casting", "K-Series Guides", "EVA", "Sungai", 30),

            // Galatama
            Joran(0, "Daido Emperor", "Rp 900.000", "Joran kaku dan responsif untuk lomba galatama ikan mas dan patin.", "", "1.8m", "Heavy", "High-Density Carbon", "Extra Fast", "Spinning", "Fuji New Concept", "Full EVA", "Galatama", 20),
            Joran(0, "Golden Fish King Gara", "Rp 780.000", "Joran galatama yang sudah teruji, memiliki daya angkat yang sangat besar.", "", "1.8m", "Extra-Heavy", "Carbon Solid", "Fast", "Spinning", "Heavy Duty Rings", "EVA & Cork", "Galatama", 28)
        )

        for (joran in joransToSeed) {
            val values = ContentValues().apply {
                put(COLUMN_NAME, joran.name)
                put(COLUMN_PRICE, joran.price)
                put(COLUMN_DESCRIPTION, joran.description)
                put(COLUMN_PHOTO, joran.photo)
                put(COLUMN_PANJANG, joran.panjang)
                put(COLUMN_POWER, joran.power)
                put(COLUMN_MATERIAL, joran.material)
                put(COLUMN_AKSI, joran.aksi)
                put(COLUMN_JENIS, joran.jenis)
                put(COLUMN_GUIDES, joran.guides)
                put(COLUMN_HANDLE, joran.handle)
                put(COLUMN_JORAN_CATEGORY, joran.category)
                put(COLUMN_JORAN_STOK, joran.stok)
            }
            db.insert(TABLE_JORAN, null, values)
        }
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Jika ada perubahan skema, tabel akan dibuat ulang
        db.execSQL("DROP TABLE IF EXISTS $TABLE_JORAN")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CART")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TRANSACTIONS")
        onCreate(db)
    }

    fun getAllJoran(kategori: String? = null): ArrayList<Joran> {
        val joranList = ArrayList<Joran>()
        val db = this.readableDatabase
        val selectQuery = if (kategori == null || kategori.equals("Semua", ignoreCase = true)) {
            "SELECT * FROM $TABLE_JORAN"
        } else {
            "SELECT * FROM $TABLE_JORAN WHERE $COLUMN_JORAN_CATEGORY = ?"
        }
        val selectionArgs = if (kategori == null || kategori.equals("Semua", ignoreCase = true)) null else arrayOf(kategori)
        val cursor: Cursor? = db.rawQuery(selectQuery, selectionArgs)
        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    joranList.add(
                        Joran(
                            id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID)),
                            name = it.getString(it.getColumnIndexOrThrow(COLUMN_NAME)),
                            price = it.getString(it.getColumnIndexOrThrow(COLUMN_PRICE)),
                            description = it.getString(it.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                            photo = it.getString(it.getColumnIndexOrThrow(COLUMN_PHOTO)),
                            panjang = it.getString(it.getColumnIndexOrThrow(COLUMN_PANJANG)),
                            power = it.getString(it.getColumnIndexOrThrow(COLUMN_POWER)),
                            material = it.getString(it.getColumnIndexOrThrow(COLUMN_MATERIAL)),
                            aksi = it.getString(it.getColumnIndexOrThrow(COLUMN_AKSI)),
                            jenis = it.getString(it.getColumnIndexOrThrow(COLUMN_JENIS)),
                            guides = it.getString(it.getColumnIndexOrThrow(COLUMN_GUIDES)),
                            handle = it.getString(it.getColumnIndexOrThrow(COLUMN_HANDLE)),
                            category = it.getString(it.getColumnIndexOrThrow(COLUMN_JORAN_CATEGORY)) ?: "Lainnya",
                            stok = it.getInt(it.getColumnIndexOrThrow(COLUMN_JORAN_STOK))
                        )
                    )
                } while (it.moveToNext())
            }
        }
        return joranList
    }

    fun addJoran(joran: Joran) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, joran.name); put(COLUMN_PRICE, joran.price); put(COLUMN_DESCRIPTION, joran.description)
            put(COLUMN_PHOTO, joran.photo); put(COLUMN_PANJANG, joran.panjang); put(COLUMN_POWER, joran.power)
            put(COLUMN_MATERIAL, joran.material); put(COLUMN_AKSI, joran.aksi); put(COLUMN_JENIS, joran.jenis)
            put(COLUMN_GUIDES, joran.guides); put(COLUMN_HANDLE, joran.handle); put(COLUMN_JORAN_CATEGORY, joran.category)
            put(COLUMN_JORAN_STOK, joran.stok)
        }
        db.insert(TABLE_JORAN, null, values)
    }

    fun updateJoran(joran: Joran): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, joran.name); put(COLUMN_PRICE, joran.price); put(COLUMN_DESCRIPTION, joran.description)
            put(COLUMN_PHOTO, joran.photo); put(COLUMN_PANJANG, joran.panjang); put(COLUMN_POWER, joran.power)
            put(COLUMN_MATERIAL, joran.material); put(COLUMN_AKSI, joran.aksi); put(COLUMN_JENIS, joran.jenis)
            put(COLUMN_GUIDES, joran.guides); put(COLUMN_HANDLE, joran.handle); put(COLUMN_JORAN_CATEGORY, joran.category)
            put(COLUMN_JORAN_STOK, joran.stok)
        }
        return db.update(TABLE_JORAN, values, "$COLUMN_ID = ?", arrayOf(joran.id.toString()))
    }

    fun deleteJoran(joran: Joran) {
        val db = this.writableDatabase
        db.delete(TABLE_JORAN, "$COLUMN_ID = ?", arrayOf(joran.id.toString()))
    }

    fun getJoranById(joranId: Int): Joran? {
        val db = this.readableDatabase
        var joran: Joran? = null
        val cursor = db.query(TABLE_JORAN, null, "$COLUMN_ID = ?", arrayOf(joranId.toString()), null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                joran = Joran(
                    id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID)),
                    name = it.getString(it.getColumnIndexOrThrow(COLUMN_NAME)),
                    price = it.getString(it.getColumnIndexOrThrow(COLUMN_PRICE)),
                    description = it.getString(it.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                    photo = it.getString(it.getColumnIndexOrThrow(COLUMN_PHOTO)),
                    panjang = it.getString(it.getColumnIndexOrThrow(COLUMN_PANJANG)),
                    power = it.getString(it.getColumnIndexOrThrow(COLUMN_POWER)),
                    material = it.getString(it.getColumnIndexOrThrow(COLUMN_MATERIAL)),
                    aksi = it.getString(it.getColumnIndexOrThrow(COLUMN_AKSI)),
                    jenis = it.getString(it.getColumnIndexOrThrow(COLUMN_JENIS)),
                    guides = it.getString(it.getColumnIndexOrThrow(COLUMN_GUIDES)),
                    handle = it.getString(it.getColumnIndexOrThrow(COLUMN_HANDLE)),
                    category = it.getString(it.getColumnIndexOrThrow(COLUMN_JORAN_CATEGORY)) ?: "Lainnya",
                    stok = it.getInt(it.getColumnIndexOrThrow(COLUMN_JORAN_STOK))
                )
            }
        }
        return joran
    }

    fun getQuantityInCart(userId: Int, joranId: Int): Int {
        val db = this.readableDatabase
        var quantity = 0
        val query = "SELECT $COLUMN_CART_JUMLAH FROM $TABLE_CART WHERE $COLUMN_CART_USER_ID = ? AND $COLUMN_CART_JORAN_ID = ?"
        val cursor = db.rawQuery(query, arrayOf(userId.toString(), joranId.toString()))
        if (cursor.moveToFirst()) {
            quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_JUMLAH))
        }
        cursor.close()
        return quantity
    }

    fun addToCart(userId: Int, joranId: Int, jumlah: Int) {
        val db = this.writableDatabase
        val query = "SELECT * FROM $TABLE_CART WHERE $COLUMN_CART_USER_ID = ? AND $COLUMN_CART_JORAN_ID = ?"
        val cursor = db.rawQuery(query, arrayOf(userId.toString(), joranId.toString()))
        if (cursor.moveToFirst()) {
            val existingJumlah = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_JUMLAH))
            val newJumlah = existingJumlah + jumlah
            val cartId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_ID))
            val values = ContentValues().apply { put(COLUMN_CART_JUMLAH, newJumlah) }
            db.update(TABLE_CART, values, "$COLUMN_CART_ID = ?", arrayOf(cartId.toString()))
        } else {
            val values = ContentValues().apply {
                put(COLUMN_CART_USER_ID, userId)
                put(COLUMN_CART_JORAN_ID, joranId)
                put(COLUMN_CART_JUMLAH, jumlah)
            }
            db.insert(TABLE_CART, null, values)
        }
        cursor.close()
    }

    fun getCartItems(userId: Int): ArrayList<CartItem> {
        val cartItems = ArrayList<CartItem>()
        val db = this.readableDatabase
        val selectQuery = "SELECT c.$COLUMN_CART_ID, c.$COLUMN_CART_JORAN_ID, c.$COLUMN_CART_JUMLAH, " +
                "j.$COLUMN_NAME, j.$COLUMN_PRICE, j.$COLUMN_PHOTO FROM $TABLE_CART c " +
                "INNER JOIN $TABLE_JORAN j ON c.$COLUMN_CART_JORAN_ID = j.$COLUMN_ID " +
                "WHERE c.$COLUMN_CART_USER_ID = ?"
        val cursor: Cursor? = db.rawQuery(selectQuery, arrayOf(userId.toString()))
        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    cartItems.add(
                        CartItem(
                            id = it.getInt(it.getColumnIndexOrThrow(COLUMN_CART_ID)),
                            userId = userId,
                            joranId = it.getInt(it.getColumnIndexOrThrow(COLUMN_CART_JORAN_ID)),
                            jumlah = it.getInt(it.getColumnIndexOrThrow(COLUMN_CART_JUMLAH)),
                            joranName = it.getString(it.getColumnIndexOrThrow(COLUMN_NAME)),
                            joranPrice = it.getString(it.getColumnIndexOrThrow(COLUMN_PRICE)),
                            joranPhoto = it.getString(it.getColumnIndexOrThrow(COLUMN_PHOTO))
                        )
                    )
                } while (it.moveToNext())
            }
        }
        return cartItems
    }

    fun deleteCartItem(cartItemId: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_CART, "$COLUMN_CART_ID = ?", arrayOf(cartItemId.toString()))
    }

    fun addUser(Username: String, password: String, email: String, alamat: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, Username); put(COLUMN_PASSWORD, password); put(COLUMN_EMAIL, email)
            put(COLUMN_USER_ALAMAT, alamat); put(COLUMN_USER_ROLE, "user"); put(COLUMN_USER_PHOTO, "")
        }
        return db.insert(TABLE_USERS, null, values)
    }

    fun checkUser(email: String, pass: String): String? {
        val db = this.readableDatabase
        var userRole: String? = null
        val cursor = db.query(TABLE_USERS, arrayOf(COLUMN_USER_ROLE), "$COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?", arrayOf(email, pass), null, null, null)
        if (cursor.moveToFirst()) {
            userRole = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_ROLE))
        }
        cursor.close()
        return userRole
    }

    fun getUser(email: String): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_USERS WHERE $COLUMN_EMAIL = ?", arrayOf(email))
    }

    fun updateUser(id: String, username: String, email: String, alamat: String, photo: String): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_USERNAME, username); put(COLUMN_EMAIL, email)
            put(COLUMN_USER_ALAMAT, alamat); put(COLUMN_USER_PHOTO, photo)
        }
        return db.update(TABLE_USERS, contentValues, "$COLUMN_USER_ID = ?", arrayOf(id))
    }

    fun getUserByEmail(email: String): User? {
        val db = this.readableDatabase
        var user: User? = null
        val cursor = db.query(TABLE_USERS, null, "$COLUMN_EMAIL = ?", arrayOf(email), null, null, null)
        if (cursor.moveToFirst()) {
            user = User(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)),
                username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME)),
                email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                alamat = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_ALAMAT)),
                photo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_PHOTO)) ?: "",
                role = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_ROLE)) ?: "user"
            )
        }
        cursor.close()
        return user
    }

    fun updateUserPassword(email: String, newPassword: String): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply { put(COLUMN_PASSWORD, newPassword) }
        return db.update(TABLE_USERS, values, "$COLUMN_EMAIL = ?", arrayOf(email))
    }

    fun checkout(userId: Int, cartItems: List<CartItem>, totalPrice: String): Boolean {
        val db = this.writableDatabase
        db.beginTransaction()
        try {
            for (item in cartItems) {
                val cursor = db.query(TABLE_JORAN, arrayOf(COLUMN_JORAN_STOK), "$COLUMN_ID = ?", arrayOf(item.joranId.toString()), null, null, null)
                if (cursor.moveToFirst()) {
                    val currentStock = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_JORAN_STOK))
                    val newStock = currentStock - item.jumlah
                    if (newStock < 0) {
                        db.endTransaction()
                        cursor.close()
                        return false
                    }
                    val values = ContentValues().apply { put(COLUMN_JORAN_STOK, newStock) }
                    db.update(TABLE_JORAN, values, "$COLUMN_ID = ?", arrayOf(item.joranId.toString()))
                }
                cursor.close()
            }

            val detailsBuilder = StringBuilder()
            for (item in cartItems) {
                detailsBuilder.append("${item.joranName} (x${item.jumlah})\n")
            }

            val transactionValues = ContentValues().apply {
                put(COLUMN_TRANS_USER_ID, userId)
                put(COLUMN_TRANS_DETAILS, detailsBuilder.toString())
                put(COLUMN_TRANS_TOTAL_PRICE, totalPrice)
                put(COLUMN_TRANS_DATE, java.text.SimpleDateFormat("dd-MM-yyyy HH:mm", java.util.Locale.getDefault()).format(java.util.Date()))
            }
            db.insert(TABLE_TRANSACTIONS, null, transactionValues)

            db.delete(TABLE_CART, "$COLUMN_CART_USER_ID = ?", arrayOf(userId.toString()))

            db.setTransactionSuccessful()
            return true

        } catch (e: Exception) {
            e.printStackTrace()
            return false
        } finally {
            db.endTransaction()
        }
    }

    fun getTransactionHistory(userId: Int): ArrayList<Transaction> {
        val transactionList = ArrayList<Transaction>()
        val db = this.readableDatabase

        val cursor = db.query(TABLE_TRANSACTIONS, null, "$COLUMN_TRANS_USER_ID = ?", arrayOf(userId.toString()), null, null, "$COLUMN_TRANS_ID DESC")

        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    val transaction = Transaction(
                        id = it.getInt(it.getColumnIndexOrThrow(COLUMN_TRANS_ID)),
                        userId = it.getInt(it.getColumnIndexOrThrow(COLUMN_TRANS_USER_ID)),
                        details = it.getString(it.getColumnIndexOrThrow(COLUMN_TRANS_DETAILS)),
                        totalPrice = it.getString(it.getColumnIndexOrThrow(COLUMN_TRANS_TOTAL_PRICE)),
                        transactionDate = it.getString(it.getColumnIndexOrThrow(COLUMN_TRANS_DATE))
                    )
                    transactionList.add(transaction)
                } while (it.moveToNext())
            }
        }
        return transactionList
    }
}
