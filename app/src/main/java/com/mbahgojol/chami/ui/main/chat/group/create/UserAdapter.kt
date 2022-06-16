package com.mbahgojol.chami.ui.main.chat.group.create

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.mbahgojol.chami.R
import com.mbahgojol.chami.data.model.Users
import com.mbahgojol.chami.databinding.ItemSearchBinding

class UserAdapter constructor(
    private val data: MutableList<Users> = mutableListOf(),
    private var listener: (Users) -> Unit
) : RecyclerView.Adapter<UserAdapter.SearchUserViewHolder>() {

    val selectedData = mutableListOf<Users>()

    fun setData(data: MutableList<Users>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    inner class SearchUserViewHolder(private val binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var currentPos = -1

        fun bind(user: Users) {
            binding.avatar.load(user.profile_url) {
                transformations(CircleCropTransformation())
            }
            binding.tvName.text = user.username
            binding.tvDivisi.text = user.jabatan

            binding.root.setOnClickListener {
                if (currentPos == layoutPosition) {
                    binding.itemLastChat.setBackgroundColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.white
                        )
                    )
                    currentPos = -1
                    selectedData.remove(user)
                } else {
                    binding.itemLastChat.setBackgroundColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.boxChatLog
                        )
                    )
                    currentPos = layoutPosition
                    selectedData.add(user)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchUserViewHolder =
        SearchUserViewHolder(
            ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: SearchUserViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = data.size
}