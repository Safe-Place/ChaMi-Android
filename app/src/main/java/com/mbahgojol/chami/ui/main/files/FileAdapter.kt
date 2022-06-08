package com.mbahgojol.chami.ui.main.files

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mbahgojol.chami.data.model.Files
import com.mbahgojol.chami.databinding.ItemListFilesBinding

class FileAdapter constructor(
    private val data: MutableList<Files> = mutableListOf(),
    private var listener: (Files) -> Unit
) :
    RecyclerView.Adapter<FileAdapter.FileViewHolder>() {

    fun setData(data: MutableList<Files>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    inner class FileViewHolder(private val binding: ItemListFilesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Files) {
            binding.tvNamaFile.text = data.nama_file
            binding.date.text = data.create_at
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileAdapter.FileViewHolder =
        FileViewHolder(
            ItemListFilesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: FileAdapter.FileViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            listener(item)
        }
    }

    override fun getItemCount(): Int = data.size

}