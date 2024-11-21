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
            this,
            onEditClick = { mahasiswa ->
                // Handle edit button click
                val intent = Intent(this, EditActivity::class.java).apply {
                    putExtra("EXTRA_ID", mahasiswa.id)
                    putExtra("EXTRA_NAMA", mahasiswa.nama)
                    putExtra("EXTRA_NIM", mahasiswa.nim)
                    putExtra("EXTRA_JURUSAN", mahasiswa.jurusan)
                }
                startActivity(intent)
            },
            onDeleteClick = { mahasiswa ->
                // Handle delete button click
                showDeleteConfirmationDialog(mahasiswa)
            }
        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = mahasiswaAdapter
        }

        // Set hint for SearchView
        binding.searchView.queryHint = "Cari Mahasiswa"

        // Handle click on Floating Action Button (FAB) to add new mahasiswa
        binding.fab.setOnClickListener {
            startActivity(Intent(this, AddActivity::class.java))
        }

        // Set up search functionality for filtering mahasiswa
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterMahasiswa(newText)
                return true
            }
        })
        binding.searchView.setOnClickListener {
            binding.searchView.isIconified = false // Makes sure the SearchView is expanded when clicked
        }

        // Handle click on RecyclerView item for detail view
        mahasiswaAdapter.setOnItemClickListener { mahasiswa ->
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra("nama", mahasiswa.nama)
                putExtra("nim", mahasiswa.nim)
                putExtra("jurusan", mahasiswa.jurusan)
            }
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadMahasiswa()
    }

    // Load the mahasiswa data from database and update RecyclerView
    private fun loadMahasiswa() {
        val mahasiswaList = databaseHelper.getAllMahasiswa()
        mahasiswaAdapter.updateData(mahasiswaList)
    }

    // Filter mahasiswa based on query text
    private fun filterMahasiswa(query: String?) {
        val allMahasiswa = databaseHelper.getAllMahasiswa()
        val filteredList = allMahasiswa.filter {
            it.nama.contains(query ?: "", ignoreCase = true) ||
                    it.nim.contains(query ?: "", ignoreCase = true) ||
                    it.jurusan.contains(query ?: "", ignoreCase = true)
        }
        mahasiswaAdapter.updateData(filteredList)
    }

    // Show confirmation dialog for deleting mahasiswa
    private fun showDeleteConfirmationDialog(mahasiswa: Mahasiswa) {
        AlertDialog.Builder(this).apply {
            setTitle("Hapus Mahasiswa")
            setMessage("Apakah Anda yakin ingin menghapus ${mahasiswa.nama}?")
            setPositiveButton("Ya") { _, _ ->
                val deletedRows = databaseHelper.deleteMahasiswa(mahasiswa.id)
                if (deletedRows > 0) {
                    Toast.makeText(this@MainActivity, "Mahasiswa berhasil dihapus", Toast.LENGTH_SHORT).show()
                    loadMahasiswa() // Reload mahasiswa list after delete
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
