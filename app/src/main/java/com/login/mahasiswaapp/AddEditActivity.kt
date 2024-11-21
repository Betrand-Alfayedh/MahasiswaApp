package com.login.mahasiswaapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.login.mahasiswaapp.database.DatabaseHelper
import com.login.mahasiswaapp.databinding.ActivityAddEditBinding
import com.login.mahasiswaapp.model.Mahasiswa

class AddEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditBinding
    private lateinit var databaseHelper: DatabaseHelper
    private var mahasiswaId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)

        mahasiswaId = intent.getIntExtra("EXTRA_ID", -1).takeIf { it != -1 }
        val nama = intent.getStringExtra("EXTRA_NAMA")
        val nim = intent.getStringExtra("EXTRA_NIM")
        val jurusan = intent.getStringExtra("EXTRA_JURUSAN")

        if (mahasiswaId != null) {
            binding.etNama.setText(nama)
            binding.etNim.setText(nim)
            binding.etJurusan.setText(jurusan)
        }

        binding.btnSave.setOnClickListener {
            val namaInput = binding.etNama.text.toString().trim()
            val nimInput = binding.etNim.text.toString().trim()
            val jurusanInput = binding.etJurusan.text.toString().trim()

            if (namaInput.isEmpty() || nimInput.isEmpty() || jurusanInput.isEmpty()) {
                Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (mahasiswaId == null) {
                val id = databaseHelper.insertMahasiswa(
                    Mahasiswa(nama = namaInput, nim = nimInput, jurusan = jurusanInput)
                )
                if (id != -1L) {
                    Toast.makeText(this, "Mahasiswa berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Gagal menambahkan mahasiswa", Toast.LENGTH_SHORT).show()
                }
            } else {
                val updatedRows = databaseHelper.updateMahasiswa(
                    Mahasiswa(id = mahasiswaId!!, nama = namaInput, nim = nimInput, jurusan = jurusanInput)
                )
                if (updatedRows > 0) {
                    Toast.makeText(this, "Mahasiswa berhasil diperbarui", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Gagal memperbarui mahasiswa", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
