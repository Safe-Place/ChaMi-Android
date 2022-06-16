package com.mbahgojol.chami.ui.main.chat.personal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.google.firebase.firestore.ktx.toObject
import com.mbahgojol.chami.R
import com.mbahgojol.chami.data.model.ChatRoom
import com.mbahgojol.chami.data.model.Detail
import com.mbahgojol.chami.data.model.Users
import com.mbahgojol.chami.data.remote.FirestoreService
import com.mbahgojol.chami.databinding.ItemListChatBinding
import com.mbahgojol.chami.utils.DateUtils
import timber.log.Timber

class ListPersonalAdapter constructor(
    private val senderId: String,
    private val service: FirestoreService,
    private val data: MutableList<ChatRoom> = mutableListOf(),
    private var listener: (ChatRoom, Boolean) -> Unit
) :
    RecyclerView.Adapter<ListPersonalAdapter.PersonalChatViewHolder>() {

    fun setData(data: MutableList<ChatRoom>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    inner class PersonalChatViewHolder(val binding: ItemListChatBinding) :
        RecyclerView.ViewHolder(binding.root) {

        var detail: Detail? = null

        fun bind(model: ChatRoom) {
            service.getChatDetail(senderId, model.roomid)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Timber.d("Listen failed.")
                        return@addSnapshotListener
                    }

                    if (snapshot != null && snapshot.exists()) {
                        detail = snapshot.toObject<Detail>()

                        binding.tvLastMessage.text =
                            if (senderId == detail?.author_id) "me: ${detail?.last_chat}" else detail?.last_chat

                        Timber.e(detail?.isread.toString())
                        if (detail?.isread == false) {
                            service.getNotifUserByReceiver(senderId, model.receiver_id)
                                .get()
                                .addOnSuccessListener {
                                    if (it?.get("count") != null) {
                                        val count = it.get("count") as Long
                                        Timber.e(count.toString())
                                        binding.tvIncomingChat.isVisible = true
                                        binding.tvIncomingChat.text = count.toString()
                                    } else {
                                        binding.tvIncomingChat.isVisible = true
                                        binding.tvIncomingChat.text = 1.toString()
                                    }
                                }
                        } else {
                            binding.tvIncomingChat.isVisible = false
                            binding.tvIncomingChat.text = "0"
                        }
                        binding.tvDate.text =
                            detail?.last_date?.let { DateUtils.reformatToClock(it) }
                    } else {
                        Timber.e("Tidak ada List Chat")
                    }
                }

            service.getUserProfile(model.receiver_id)
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Timber.d("Listen failed.")
                        return@addSnapshotListener
                    }

                    if (snapshot != null && snapshot.exists()) {
                        val user = snapshot.toObject<Users>()

                        binding.avatar.load(user?.profile_url) {
                            transformations(CircleCropTransformation())
                        }
                        binding.tvName.text = user?.username

                        if (user?.isonline == true) {
                            binding.isOnline.backgroundTintList =
                                ContextCompat.getColorStateList(binding.root.context, R.color.green)
                        } else {
                            binding.isOnline.backgroundTintList =
                                ContextCompat.getColorStateList(binding.root.context, R.color.grey)
                        }
                    } else {
                        Timber.e("Tidak ada List Chat")
                    }
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
            listener(item, holder.detail?.isread ?: false)
        }
    }

    override fun getItemCount(): Int = data.size
}