package com.login.mahasiswaapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.login.mahasiswaapp.database.DatabaseHelper
import com.login.mahasiswaapp.databinding.ActivityAddBinding
import com.login.mahasiswaapp.model.Mahasiswa

class AddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBinding
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)

        binding.btnSave.setOnClickListener {
            val nama = binding.etNama.text.toString()
            val nim = binding.etNim.text.toString()
            val jurusan = binding.etJurusan.text.toString()

            if (nama.isBlank() || nim.isBlank() || jurusan.isBlank()) {
                Toast.makeText(this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show()
            } else {
                val mahasiswa = Mahasiswa(0, nama, nim, jurusan)
                val result = databaseHelper.insertMahasiswa(mahasiswa)

                if (result > 0) {
                    Toast.makeText(this, "Mahasiswa berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                    finish() // Kembali ke MainActivity
                } else {
                    Toast.makeText(this, "Gagal menambahkan mahasiswa", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
