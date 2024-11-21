package com.login.mahasiswaapp.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.login.mahasiswaapp.model.Mahasiswa

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "mahasiswa.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "mahasiswa"
        const val COLUMN_ID = "id"
        const val COLUMN_NAMA = "nama"
        const val COLUMN_NIM = "nim"
        const val COLUMN_JURUSAN = "jurusan"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAMA TEXT,
                $COLUMN_NIM TEXT,
                $COLUMN_JURUSAN TEXT
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertMahasiswa(mahasiswa: Mahasiswa): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAMA, mahasiswa.nama)
            put(COLUMN_NIM, mahasiswa.nim)
            put(COLUMN_JURUSAN, mahasiswa.jurusan)
        }
        return db.insert(TABLE_NAME, null, values)
    }

    fun getAllMahasiswa(): List<Mahasiswa> {
        val db = readableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)
        val mahasiswaList = mutableListOf<Mahasiswa>()

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val nama = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAMA))
                val nim = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NIM))
                val jurusan = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JURUSAN))
                mahasiswaList.add(Mahasiswa(id, nama, nim, jurusan))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return mahasiswaList
    }

    fun updateMahasiswa(mahasiswa: Mahasiswa): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAMA, mahasiswa.nama)
            put(COLUMN_NIM, mahasiswa.nim)
            put(COLUMN_JURUSAN, mahasiswa.jurusan)
        }
        return db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(mahasiswa.id.toString()))
    }

    fun deleteMahasiswa(id: Int): Int {
        val db = writableDatabase
        return db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }
}