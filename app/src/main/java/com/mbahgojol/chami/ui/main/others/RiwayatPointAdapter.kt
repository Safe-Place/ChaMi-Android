package com.mbahgojol.chami.ui.main.others

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mbahgojol.chami.R
import com.mbahgojol.chami.dummyData.RiwayatPoint
import com.mbahgojol.chami.dummyData.RiwayatTransaksi

class RiwayatPointAdapter(private val listPoint: ArrayList<RiwayatPoint>) : RecyclerView.Adapter<RiwayatPointAdapter.ListViewHolder>() {

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvJenis: TextView = itemView.findViewById(R.id.tvJenis)
        var tvNama: TextView = itemView.findViewById(R.id.namaPoint)
        var tvTanggal: TextView = itemView.findViewById(R.id.tvTanggalPoint)
        var tvNominal: TextView = itemView.findViewById(R.id.nominal)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatusPoint)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_riwayat_point, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (jenis,nama,tanggal,nominal,status) = listPoint[position]
        holder.tvJenis.text = jenis
        holder.tvNama.text = nama
        holder.tvTanggal.text = tanggal
        holder.tvNominal.text = nominal.toString()
        holder.tvStatus.text = status
//        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listChallenge[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int = listPoint.size


//    interface OnItemClickCallback {
//        fun onItemClicked(data: Challenge)
//    }
}