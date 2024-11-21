package com.login.mahasiswaapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Ambil data dari Intent
        val nama = intent.getStringExtra("nama")
        val nim = intent.getStringExtra("nim")
        val jurusan = intent.getStringExtra("jurusan")

        // Temukan TextView dari layout
        val tvNama = findViewById<TextView>(R.id.tvNama)
        val tvNIM = findViewById<TextView>(R.id.tvNIM)
        val tvJurusan = findViewById<TextView>(R.id.tvJurusan)

        // Set data ke TextView
        tvNama.text = nama
        tvNIM.text = nim
        tvJurusan.text = jurusan
    }
}
