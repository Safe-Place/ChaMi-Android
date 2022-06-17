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
import com.mbahgojol.chami.data.model.Challenges
import com.mbahgojol.chami.data.model.Peserta
import com.mbahgojol.chami.databinding.ItemListChallengeBinding
import com.mbahgojol.chami.databinding.ItemListParticipantBinding
import com.mbahgojol.chami.dummyData.Participant
import com.mbahgojol.chami.dummyData.Produk
import java.text.SimpleDateFormat
import java.util.*

class ListParticipantAdapter constructor(
    private val data: MutableList<Peserta> = mutableListOf(),
    private var listener: (Peserta) -> Unit
) :
    RecyclerView.Adapter<ListParticipantAdapter.FileViewHolder>() {

    fun setData(data: MutableList<Peserta>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    inner class FileViewHolder(private val binding: ItemListParticipantBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Peserta) {
            binding.participantAvatar.load(data.avatar_url) {
                transformations(CircleCropTransformation())
            }
            binding.participantNama.text = data.nama_user
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListParticipantAdapter.FileViewHolder =
        FileViewHolder(
            ItemListParticipantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ListParticipantAdapter.FileViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            listener(item)
        }
    }

    override fun getItemCount(): Int = data.size

}