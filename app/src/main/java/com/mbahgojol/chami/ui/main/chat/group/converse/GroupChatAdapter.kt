package com.mbahgojol.chami.ui.main.chat.group.converse

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mbahgojol.chami.data.model.ChatLog
import com.mbahgojol.chami.databinding.ItemChatPersonalSendBinding
import com.mbahgojol.chami.utils.DateUtils
import com.mbahgojol.chami.utils.TypeMsg

class GroupChatAdapter constructor(
    private val authorId: String,
    private val data: MutableList<ChatLog> = mutableListOf(),
    private var listener: (ChatLog) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun setData(data: MutableList<ChatLog>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    inner class ChatViewHolder(private val binding: ItemChatPersonalSendBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: ChatLog) {
            if (authorId == model.author_id) {
                binding.tvMessage.text = model.message
                binding.tvTimestamp.text = DateUtils.reformatToClock(model.create_date)

                binding.layoutReceiver.contenReceiver.visibility = View.GONE
                binding.layoutSender.visibility = View.VISIBLE
            } else {
                binding.layoutReceiver.contenReceiver.visibility = View.VISIBLE
                binding.layoutReceiver.tvAuthor.visibility = View.VISIBLE
                binding.layoutSender.visibility = View.GONE
                binding.layoutReceiver.tvAuthor.text = model.author_name
                binding.layoutReceiver.tvMessage.text = model.message
                binding.layoutReceiver.tvTimestamp.text =
                    DateUtils.reformatToClock(model.create_date)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            TypeMsg.TEXT.ordinal -> {
                ChatViewHolder(
                    ItemChatPersonalSendBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                ChatViewHolder(
                    ItemChatPersonalSendBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = data[position]
        when (holder) {
            is ChatViewHolder -> {
                holder.bind(item)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = data[position]
        return when (item.type) {
            TypeMsg.TEXT.ordinal -> TypeMsg.TEXT.ordinal
            else -> TypeMsg.TEXT.ordinal
        }
    }

    override fun getItemCount(): Int = data.size
}