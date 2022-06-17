package com.mbahgojol.chami.ui.main.others.supervisor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mbahgojol.chami.R
import com.mbahgojol.chami.dummyData.HistoryChallenge
import com.mbahgojol.chami.dummyData.RiwayatChallenge

class RiwayatChallengeAdapter(private val listChallenge: ArrayList<RiwayatChallenge>) : RecyclerView.Adapter<RiwayatChallengeAdapter.ListViewHolder>() {
//    private lateinit var onItemClickCallback: OnItemClickCallback

//    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
//        this.onItemClickCallback = onItemClickCallback
//    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvJudul: TextView = itemView.findViewById(R.id.judulChallenge)
        var tvDeskripsi: TextView = itemView.findViewById(R.id.descChallenge)
        var tvTenggat: TextView = itemView.findViewById(R.id.tenggat)
        var tvReward: TextView = itemView.findViewById(R.id.point)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_riwayat_celen, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (judul, desc, reward, tenggat) = listChallenge[position]
        holder.tvJudul.text = judul
        holder.tvDeskripsi.text = desc
        holder.tvReward.text = reward.toString()+" Point"
        holder.tvTenggat.text = tenggat
//        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listChallenge[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int = listChallenge.size


//    interface OnItemClickCallback {
//        fun onItemClicked(data: Challenge)
//    }
}