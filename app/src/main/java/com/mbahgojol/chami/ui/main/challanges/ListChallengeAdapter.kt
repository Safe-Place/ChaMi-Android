package com.mbahgojol.chami.ui.main.challanges

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mbahgojol.chami.R
import com.mbahgojol.chami.data.model.Challenges
import com.mbahgojol.chami.data.model.Files
import com.mbahgojol.chami.databinding.ItemListChallengeBinding
import com.mbahgojol.chami.databinding.ItemListFilesBinding
import com.mbahgojol.chami.dummyData.Challenge
import com.mbahgojol.chami.ui.main.others.TukarPointAdapter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ListChallengeAdapter constructor(
    private val data: MutableList<Challenges> = mutableListOf(),
    private var listener: (Challenges) -> Unit
) :
    RecyclerView.Adapter<ListChallengeAdapter.FileViewHolder>() {

    fun setData(data: MutableList<Challenges>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    inner class FileViewHolder(private val binding: ItemListChallengeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Challenges) {
            var tenggat = convertLongToTime(data.due_date!!)
            binding.judulChallenge.text = data.judul
            binding.descChallenge.text = data.deskripsi
            binding.tenggat.text = tenggat
            binding.status.text = "Sedang berlangsung"
            binding.point.text = data.point+" Point"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListChallengeAdapter.FileViewHolder =
        FileViewHolder(
            ItemListChallengeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ListChallengeAdapter.FileViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            listener(item)
        }
    }

    override fun getItemCount(): Int = data.size

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("dd-MM-yyyy")
        return format.format(date)
    }

}
//class ListChallengeAdapter(private val listChallenge: ArrayList<Challenges>) : RecyclerView.Adapter<ListChallengeAdapter.ListViewHolder>() {
//    private lateinit var onItemClickCallback: OnItemClickCallback
//
//    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
//        this.onItemClickCallback = onItemClickCallback
//    }
//
//    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        var tvJudul: TextView = itemView.findViewById(R.id.judulChallenge)
//        var tvDeskripsi: TextView = itemView.findViewById(R.id.descChallenge)
//        var tvTenggat: TextView = itemView.findViewById(R.id.tenggat)
//        var tvReward: TextView = itemView.findViewById(R.id.point)
//        var tvStatus: TextView = itemView.findViewById(R.id.status)
//
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
//        val view: View =
//            LayoutInflater.from(parent.context).inflate(R.layout.item_list_challenge, parent, false)
//        return ListViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
//        val (judul, desc, reward, tenggat) = listChallenge[position]
//        val tenggatString = convertLongToTime(tenggat!!)
//        holder.tvJudul.text = judul
//        holder.tvDeskripsi.text = desc
//        holder.tvReward.text = reward.toString()+" Point"
//        holder.tvTenggat.text = tenggatString
//        holder.tvStatus.text = "Sedang berlangsung"
//        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listChallenge[holder.adapterPosition]) }
//    }
//
//    override fun getItemCount(): Int = listChallenge.size
//
//
//    interface OnItemClickCallback {
//        fun onItemClicked(data: Challenges)
//    }
//
//    fun convertLongToTime(time: Long): String {
//        val date = Date(time)
//        val format = SimpleDateFormat("dd-MM-yyyy")
//        return format.format(date)
//    }
//}