package com.dikaramadani.sumision_akhir_android

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.dikaramadani.sumision_akhir_android.DatabaseHelper.Companion.COLUMN_EMAIL
import com.dikaramadani.sumision_akhir_android.DatabaseHelper.Companion.COLUMN_PASSWORD
import com.dikaramadani.sumision_akhir_android.DatabaseHelper.Companion.COLUMN_USERNAME
import com.dikaramadani.sumision_akhir_android.DatabaseHelper.Companion.COLUMN_USER_ALAMAT
import com.dikaramadani.sumision_akhir_android.DatabaseHelper.Companion.TABLE_USERS

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "joran.db"
        private const val DATABASE_VERSION = 5 // Versi dinaikkan untuk memicu onUpgrade
        const val TABLE_NAME = "joran"
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


        const val TABLE_USERS = "users"
        const val COLUMN_USER_ID = "id"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_USER_ALAMAT = "alamat"
        // ... di dalam companion object di DatabaseHelper.kt
        const val COLUMN_USER_ROLE = "role"

        const val COLUMN_USER_PHOTO = "photo"

    }



    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NAME TEXT, " +
                "$COLUMN_PRICE TEXT, " +
                "$COLUMN_DESCRIPTION TEXT, " +
                "$COLUMN_PHOTO TEXT, " +
                "$COLUMN_PANJANG TEXT, " +
                "$COLUMN_POWER TEXT, " +
                "$COLUMN_MATERIAL TEXT, " +
                "$COLUMN_AKSI TEXT, " +
                "$COLUMN_JENIS TEXT, " +
                "$COLUMN_GUIDES TEXT, " +
                "$COLUMN_HANDLE TEXT)"
        db.execSQL(createTable)

        val createUserTable = "CREATE TABLE $TABLE_USERS (" +
                "$COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_USERNAME TEXT, " +
                "$COLUMN_PASSWORD TEXT, " +
                "$COLUMN_EMAIL TEXT, " +
                "$COLUMN_USER_ALAMAT TEXT, " +  // <-- PERBAIKAN: Tambahkan koma dan operator +
                "$COLUMN_USER_ROLE TEXT, " +
                "$COLUMN_USER_PHOTO TEXT)"
        db.execSQL(createUserTable)

        val adminValues = ContentValues().apply {
            put(COLUMN_USERNAME, "Admin Dika")
            put(COLUMN_PASSWORD, "admin123")
            put(COLUMN_EMAIL, "admin@gmail.com")
            put(COLUMN_USER_ALAMAT, "Kantor Pusat")
            put(COLUMN_USER_ROLE, "admin")
            put(COLUMN_USER_PHOTO, "") // <-- TAMBAHKAN INI
        }
        db.insert(TABLE_USERS, null, adminValues)
// --------------------------------------------------------
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    fun getAllJoran(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

    fun addJoran(joran: Joran) {
        val db = this.writableDatabase
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
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun updateJoran(joran: Joran): Int {
        val db = this.writableDatabase
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
        }
        return db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(joran.id.toString()))
    }

    fun deleteJoran(joran: Joran) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(joran.id.toString()))
        db.close()
    }

    fun addUser(Username: String, password: String, email: String, alamat: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, Username)
            put(COLUMN_PASSWORD, password)
            put(COLUMN_EMAIL, email)
            put(COLUMN_USER_ALAMAT, alamat)
            put(COLUMN_USER_ROLE, "user")
            put(COLUMN_USER_PHOTO, "") // <-- TAMBAHKAN INI
        }
        val newRowId = db.insert(TABLE_USERS, null, values)
        db.close()
        return newRowId
    }

    fun checkUser(email: String, pass: String): String? { // Mengembalikan String (role) atau null
        val db = this.readableDatabase
        var userRole: String? = null // Variabel untuk menyimpan role
        val columns = arrayOf(COLUMN_USER_ROLE) // Kolom yang ingin kita ambil adalah 'role'
        val selection = "$COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?"
        val selectionArgs = arrayOf(email, pass)
        val cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null)
        if (cursor.moveToFirst()) { // Jika data ditemukan
            // Ambil nilai dari kolom 'role'
            userRole = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_ROLE))
        }
        cursor.close()
        db.close()
        return userRole // Kembalikan role ("admin"/"user") atau null
    }

    fun getUser(email: String): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_USERS WHERE $COLUMN_EMAIL = ?", arrayOf(email))
    }

    fun updateUser(id: String, username: String, email: String, alamat: String, photo: String): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_EMAIL, email)
            put(COLUMN_USER_ALAMAT, alamat)
            put(COLUMN_USER_PHOTO, photo)
        }
        return db.update(TABLE_USERS, contentValues, "$COLUMN_USER_ID = ?", arrayOf(id))
    }

    fun getUserByEmail(email: String): User? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_USERS,
            // ▼▼▼ TAMBAHKAN COLUMN_USER_PHOTO DI SINI ▼▼▼
            arrayOf(COLUMN_USER_ID, COLUMN_USERNAME, COLUMN_EMAIL, COLUMN_USER_ALAMAT, COLUMN_USER_PHOTO),
            "$COLUMN_EMAIL = ?",
            arrayOf(email),
            null, null, null
        )

        var user: User? = null
        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID))
            val username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME))
            val userEmail = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
            val alamat = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_ALAMAT))

            // ▼▼▼ TAMBAHKAN INI UNTUK MENGAMBIL FOTO ▼▼▼
            val photo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_PHOTO)) ?: ""

            // Buat objek User dengan menyertakan foto
            user = User(id, username, userEmail, alamat, photo) // Pastikan data class User Anda menerima 'photo'
        }

        cursor.close()
        db.close()
        return user
    }

    /**
     * Fungsi untuk memperbarui password seorang pengguna berdasarkan email mereka.
     */
    fun updateUserPassword(email: String, newPassword: String): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_PASSWORD, newPassword)
        }
        // Update tabel users dimana kolom email cocok dengan email yang diberikan
        return db.update(TABLE_USERS, values, "$COLUMN_EMAIL = ?", arrayOf(email))
    }
}