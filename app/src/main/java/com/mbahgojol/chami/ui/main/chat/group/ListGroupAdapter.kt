package com.mbahgojol.chami.ui.main.chat.group

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.mbahgojol.chami.data.model.GrupChat
import com.mbahgojol.chami.data.remote.FirestoreService
import com.mbahgojol.chami.databinding.ItemListChatBinding
import com.mbahgojol.chami.utils.DateUtils
import timber.log.Timber

class ListGroupAdapter constructor(
    private val senderId: String,
    private val service: FirestoreService,
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
            service.getNotifCountGroup(model.id, senderId)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Timber.d("Listen failed.")
                        return@addSnapshotListener
                    }

                    if (snapshot != null && snapshot.exists()) {
                        if (snapshot.getLong("count") != null) {
                            val count = snapshot.getLong("count")
                            val isRead = snapshot.getBoolean("isRead")
                            binding.tvIncomingChat.isVisible = count != 0L && isRead == false
                            if (count != 0L) {
                                binding.tvIncomingChat.text = count.toString()
                            }
                        }
                    }
                }

            binding.isOnline.isVisible = false
            binding.tvName.text = model.namaGroup
            var author = model.lastAuthor.trim().split("\\s+".toRegex())[0]
            if (author == "") author = "me"
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