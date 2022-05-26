package com.mbahgojol.chami.ui.main.chat.personal

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.mbahgojol.chami.data.HistoryChatModel
import com.mbahgojol.chami.databinding.ItemListChatBinding
import timber.log.Timber

class PersonalChatAdapter constructor(private val data: MutableList<HistoryChatModel> = mutableListOf()) :
    RecyclerView.Adapter<PersonalChatAdapter.PersonalChatViewHolder>() {

    fun setData(data: MutableList<HistoryChatModel>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    class PersonalChatViewHolder(private val binding: ItemListChatBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: HistoryChatModel) {
            binding.avatar.load(model.profile_url) {
                transformations(CircleCropTransformation())
            }
            binding.tvName.text = model.username
            binding.tvLastMessage.text = model.last_chat
            binding.tvIncomingChat.isVisible = model.isread == "N"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonalChatViewHolder =
        PersonalChatViewHolder(
            ItemListChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: PersonalChatViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = data.size
}