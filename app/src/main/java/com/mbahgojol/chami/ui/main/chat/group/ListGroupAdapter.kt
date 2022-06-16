package com.mbahgojol.chami.ui.main.chat.group

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.mbahgojol.chami.data.model.GrupChat
import com.mbahgojol.chami.databinding.ItemListChatBinding
import com.mbahgojol.chami.utils.DateUtils

class ListGroupAdapter constructor(
    private val senderId: String,
    private val data: MutableList<GrupChat> = mutableListOf(),
    private val listener: (GrupChat) -> Unit
) : RecyclerView.Adapter<ListGroupAdapter.PersonalChatViewHolder>() {

    fun setData(data: MutableList<GrupChat>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    inner class PersonalChatViewHolder(val binding: ItemListChatBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: GrupChat) {
            binding.isOnline.isVisible = false
            binding.tvName.text = model.namaGroup
            val author = model.lastAuthor.trim().split("\\s+".toRegex())[0]
            binding.tvLastMessage.text =
                if (senderId == model.lastAuthorId) "me: ${model.lastChat}" else "$author: ${model.lastChat}"
            binding.tvDate.text = model.lastDate.let { DateUtils.reformatToClock(it) }
            binding.avatar.load(model.imgGroup) {
                transformations(CircleCropTransformation())
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