package com.login.mahasiswaapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.login.mahasiswaapp.R
import com.login.mahasiswaapp.model.Mahasiswa

class MahasiswaAdapter(
    private var mahasiswaList: MutableList<Mahasiswa>,
    private val onEditClick: (Mahasiswa) -> Unit,
    private val onDeleteClick: (Mahasiswa) -> Unit
) : RecyclerView.Adapter<MahasiswaAdapter.MahasiswaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MahasiswaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mahasiswa, parent, false)
        return MahasiswaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MahasiswaViewHolder, position: Int) {
        val mahasiswa = mahasiswaList[position]
        holder.bind(mahasiswa)
        holder.ivEdit.setOnClickListener { onEditClick(mahasiswa) }
        holder.ivDelete.setOnClickListener { onDeleteClick(mahasiswa) }
    }

    override fun getItemCount(): Int = mahasiswaList.size

    fun updateData(newMahasiswaList: List<Mahasiswa>) {
        mahasiswaList.clear()
        mahasiswaList.addAll(newMahasiswaList)
        notifyDataSetChanged()
    }

    inner class MahasiswaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNama: TextView = itemView.findViewById(R.id.tvNama)
        private val tvNim: TextView = itemView.findViewById(R.id.tvNim)
        private val tvJurusan: TextView = itemView.findViewById(R.id.tvJurusan)
        val ivEdit: ImageView = itemView.findViewById(R.id.ivEdit)
        val ivDelete: ImageView = itemView.findViewById(R.id.ivDelete)

        fun bind(mahasiswa: Mahasiswa) {
            tvNama.text = mahasiswa.nama
            tvNim.text = "NIM: ${mahasiswa.nim}"
            tvJurusan.text = "Jurusan: ${mahasiswa.jurusan}"
        }
    }
}
