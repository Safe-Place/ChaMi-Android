package com.mbahgojol.chami.ui.main.others

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.mbahgojol.chami.R
import com.mbahgojol.chami.dummyData.Participant
import com.mbahgojol.chami.dummyData.Validasi

class ApprovalAdapter(private val listValidasi: ArrayList<Validasi>) : RecyclerView.Adapter<ApprovalAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivFoto: ImageView = itemView.findViewById(R.id.avatar1)
        var tvNama: TextView = itemView.findViewById(R.id.tvName1)
        var tvDivisi: TextView = itemView.findViewById(R.id.tvJabatan1)
        var tvDeskripsi: TextView = itemView.findViewById(R.id.tvUbah1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_validasi, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (foto,nama,divisi,deskripsi) = listValidasi[position]
        holder.ivFoto.load(foto){
            transformations(CircleCropTransformation())
        }
        holder.tvNama.text = nama
        holder.tvDivisi.text = divisi
        holder.tvDeskripsi.text = deskripsi
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listValidasi[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int = listValidasi.size


    interface OnItemClickCallback {
        fun onItemClicked(data: Validasi)
    }
}