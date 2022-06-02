package com.mbahgojol.chami.ui.main.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.mbahgojol.chami.data.model.Users
import com.mbahgojol.chami.databinding.ItemSearchBinding

class SearchUserAdapter constructor(
    private val data: MutableList<Users> = mutableListOf(),
    private var listener: (Users) -> Unit
) :
    RecyclerView.Adapter<SearchUserAdapter.SearchUserViewHolder>() {

    fun setData(data: MutableList<Users>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    inner class SearchUserViewHolder(private val binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: Users) {
            binding.avatar.load(user.profile_url) {
                transformations(CircleCropTransformation())
            }
            binding.tvName.text = user.username
            binding.tvDivisi.text = user.jabatan

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchUserViewHolder =
        SearchUserViewHolder(
            ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: SearchUserViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            listener(item)
        }
    }

    override fun getItemCount(): Int = data.size
}