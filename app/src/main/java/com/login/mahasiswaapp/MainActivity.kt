package com.login.mahasiswaapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.login.mahasiswaapp.adapter.MahasiswaAdapter
import com.login.mahasiswaapp.database.DatabaseHelper
import com.login.mahasiswaapp.databinding.ActivityMainBinding
import com.login.mahasiswaapp.model.Mahasiswa

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var mahasiswaAdapter: MahasiswaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)
        mahasiswaAdapter = MahasiswaAdapter(
            mutableListOf(),
            { mahasiswa ->
                val intent = Intent(this, AddEditActivity::class.java)
                intent.putExtra("EXTRA_ID", mahasiswa.id)
                intent.putExtra("EXTRA_NAMA", mahasiswa.nama)
                intent.putExtra("EXTRA_NIM", mahasiswa.nim)
                intent.putExtra("EXTRA_JURUSAN", mahasiswa.jurusan)
                startActivity(intent)
            },
            { mahasiswa ->
                showDeleteConfirmationDialog(mahasiswa)
            }
        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = mahasiswaAdapter
        }

        binding.fab.setOnClickListener {
            startActivity(Intent(this, AddEditActivity::class.java))
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterMahasiswa(newText)
                return true
            }
        })
    }

    override fun onResume() {
        super.onResume()
        loadMahasiswa()
    }

    private fun loadMahasiswa() {
        val mahasiswaList = databaseHelper.getAllMahasiswa()
        mahasiswaAdapter.updateData(mahasiswaList)
    }

    private fun filterMahasiswa(query: String?) {
        val allMahasiswa = databaseHelper.getAllMahasiswa()
        val filteredList = allMahasiswa.filter {
            it.nama.contains(query ?: "", ignoreCase = true) ||
                    it.nim.contains(query ?: "", ignoreCase = true) ||
                    it.jurusan.contains(query ?: "", ignoreCase = true)
        }
        mahasiswaAdapter.updateData(filteredList)
    }

    private fun showDeleteConfirmationDialog(mahasiswa: Mahasiswa) {
        AlertDialog.Builder(this).apply {
            setTitle("Hapus Mahasiswa")
            setMessage("Apakah Anda yakin ingin menghapus ${mahasiswa.nama}?")
            setPositiveButton("Ya") { _, _ ->
                val deletedRows = databaseHelper.deleteMahasiswa(mahasiswa.id)
                if (deletedRows > 0) {
                    Toast.makeText(this@MainActivity, "Mahasiswa berhasil dihapus", Toast.LENGTH_SHORT).show()
                    loadMahasiswa()
                } else {
                    Toast.makeText(this@MainActivity, "Gagal menghapus mahasiswa", Toast.LENGTH_SHORT).show()
                }
            }
            setNegativeButton("Batal", null)
            create()
            show()
        }
    }
}
