package com.login.mahasiswaapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.login.mahasiswaapp.database.DatabaseHelper
import com.login.mahasiswaapp.databinding.ActivityEditBinding
import com.login.mahasiswaapp.model.Mahasiswa

class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)

        // Ambil data yang dikirim dari MainActivity
        val id = intent.getIntExtra("EXTRA_ID", -1)
        val nama = intent.getStringExtra("EXTRA_NAMA") ?: ""
        val nim = intent.getStringExtra("EXTRA_NIM") ?: ""
        val jurusan = intent.getStringExtra("EXTRA_JURUSAN") ?: ""

        // Tampilkan data di form
        binding.etNama.setText(nama)
        binding.etNim.setText(nim)
        binding.etJurusan.setText(jurusan)

        binding.btnSave.setOnClickListener {
            val updatedNama = binding.etNama.text.toString()
            val updatedNim = binding.etNim.text.toString()
            val updatedJurusan = binding.etJurusan.text.toString()

            if (updatedNama.isBlank() || updatedNim.isBlank() || updatedJurusan.isBlank()) {
                Toast.makeText(this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show()
            } else {
                val updatedMahasiswa = Mahasiswa(id, updatedNama, updatedNim, updatedJurusan)
                val result = databaseHelper.updateMahasiswa(updatedMahasiswa)

                if (result > 0) {
                    Toast.makeText(this, "Data mahasiswa berhasil diperbarui", Toast.LENGTH_SHORT).show()
                    finish() // Kembali ke MainActivity
                } else {
                    Toast.makeText(this, "Gagal memperbarui data mahasiswa", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
