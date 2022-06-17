package com.mbahgojol.chami.ui.main.others

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mbahgojol.chami.R
import com.mbahgojol.chami.dummyData.RiwayatTransaksi

class DaftarTransaksiAdapter(private val listTransaksi: ArrayList<RiwayatTransaksi>) : RecyclerView.Adapter<DaftarTransaksiAdapter.ListViewHolder>() {
//    private lateinit var onItemClickCallback: OnItemClickCallback

//    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
//        this.onItemClickCallback = onItemClickCallback
//    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTanggal: TextView = itemView.findViewById(R.id.tvTanggal)
        var tvNama: TextView = itemView.findViewById(R.id.namaProduk)
        var tvHarga: TextView = itemView.findViewById(R.id.tvHarga)
        var tvStatus: TextView = itemView.findViewById(R.id.tvStatus)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_daftar_trx, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (tanggal, nama, harga, status) = listTransaksi[position]
        holder.tvTanggal.text = tanggal
        holder.tvNama.text = nama
        holder.tvHarga.text = harga.toString()
        holder.tvStatus.text = status
//        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listChallenge[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int = listTransaksi.size


//    interface OnItemClickCallback {
//        fun onItemClicked(data: Challenge)
//    }
}