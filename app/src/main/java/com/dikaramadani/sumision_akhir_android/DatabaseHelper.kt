package com.dikaramadani.sumision_akhir_android

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "joran.db"
        private const val DATABASE_VERSION = 1
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
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NAME TEXT, " +
                "$COLUMN_PRICE TEXT, " +
                "$COLUMN_DESCRIPTION TEXT, " +
                "$COLUMN_PHOTO INTEGER, " +
                "$COLUMN_PANJANG TEXT, " +
                "$COLUMN_POWER TEXT, " +
                "$COLUMN_MATERIAL TEXT, " +
                "$COLUMN_AKSI TEXT, " +
                "$COLUMN_JENIS TEXT, " +
                "$COLUMN_GUIDES TEXT, " +
                "$COLUMN_HANDLE TEXT" +
                ")"
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}