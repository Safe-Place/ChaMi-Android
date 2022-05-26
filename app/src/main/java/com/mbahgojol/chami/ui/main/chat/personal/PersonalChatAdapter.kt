package com.mbahgojol.chami.ui.main.chat.personal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.mbahgojol.chami.R
import com.mbahgojol.chami.data.HistoryChatModel
import com.mbahgojol.chami.databinding.ItemListChatBinding

class PersonalChatAdapter constructor(
    private val data: MutableList<HistoryChatModel> = mutableListOf(),
    private var listener: (HistoryChatModel) -> Unit
) :
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

            if (model.isonline) {
                binding.isOnline.backgroundTintList =
                    ContextCompat.getColorStateList(binding.root.context, R.color.green)
            } else {
                binding.isOnline.backgroundTintList =
                    ContextCompat.getColorStateList(binding.root.context, R.color.grey)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonalChatViewHolder =
        PersonalChatViewHolder(
            ItemListChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: PersonalChatViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            listener(item)
        }
    }

    override fun getItemCount(): Int = data.size
}