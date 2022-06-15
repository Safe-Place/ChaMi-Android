package com.mbahgojol.chami.ui.main.challanges

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mbahgojol.chami.R
import com.mbahgojol.chami.dummyData.Challenge
import com.mbahgojol.chami.ui.main.others.TukarPointAdapter

class ListChallengeAdapter(private val listChallenge: ArrayList<Challenge>) : RecyclerView.Adapter<ListChallengeAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvJudul: TextView = itemView.findViewById(R.id.judulChallenge)
        var tvDeskripsi: TextView = itemView.findViewById(R.id.descChallenge)
        var tvTenggat: TextView = itemView.findViewById(R.id.tenggat)
        var tvReward: TextView = itemView.findViewById(R.id.point)
        var tvStatus: TextView = itemView.findViewById(R.id.status)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_challenge, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (judul, desc, reward, tenggat, status) = listChallenge[position]
        holder.tvJudul.text = judul
        holder.tvDeskripsi.text = desc
        holder.tvReward.text = reward.toString()
        holder.tvTenggat.text = tenggat
        holder.tvStatus.text = status
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listChallenge[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int = listChallenge.size


    interface OnItemClickCallback {
        fun onItemClicked(data: Challenge)
    }
}