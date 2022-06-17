package com.mbahgojol.chami.ui.main.others

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.mbahgojol.chami.R
import com.mbahgojol.chami.dummyData.Challenge
import com.mbahgojol.chami.dummyData.Produk

class TukarPointAdapter(private val listProduk: ArrayList<Produk>) : RecyclerView.Adapter<TukarPointAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivFoto: ImageView = itemView.findViewById(R.id.fotoProduk)
        var tvNama: TextView = itemView.findViewById(R.id.tvNamaProduk)
        var tvHarga: TextView = itemView.findViewById(R.id.tvHargaJual)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_produk, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (foto,nama,harga) = listProduk[position]
        holder.ivFoto.load(foto)
        holder.tvNama.text = nama
        holder.tvHarga.text = harga.toString()
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listProduk[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int = listProduk.size


    interface OnItemClickCallback {
        fun onItemClicked(data: Produk)
    }
}