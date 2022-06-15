package com.mbahgojol.chami.ui.main.challanges

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
import com.mbahgojol.chami.dummyData.Produk

class ListParticipantAdapter(private val listParticipant: ArrayList<Participant>) : RecyclerView.Adapter<ListParticipantAdapter.ListViewHolder>() {
//    private lateinit var onItemClickCallback: OnItemClickCallback
//
//    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
//        this.onItemClickCallback = onItemClickCallback
//    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivFoto: ImageView = itemView.findViewById(R.id.participantAvatar)
        var tvNama: TextView = itemView.findViewById(R.id.participantNama)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_participant, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (foto,nama) = listParticipant[position]
        holder.ivFoto.load(foto){
            transformations(CircleCropTransformation())
        }
        holder.tvNama.text = nama
//        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listProduk[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int = listParticipant.size


    interface OnItemClickCallback {
        fun onItemClicked(data: Participant)
    }
}