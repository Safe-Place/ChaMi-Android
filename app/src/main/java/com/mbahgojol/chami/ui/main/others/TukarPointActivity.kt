package com.mbahgojol.chami.ui.main.others

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mbahgojol.chami.R
import com.mbahgojol.chami.databinding.ActivityRiwayatPointBinding
import com.mbahgojol.chami.databinding.ActivityTukarPointBinding
import com.mbahgojol.chami.dummyData.Produk
import com.mbahgojol.chami.dummyData.RiwayatPoint
import com.mbahgojol.chami.utils.BaseActivity

class TukarPointActivity : BaseActivity() {
    private lateinit var binding : ActivityTukarPointBinding

    private lateinit var rvProduk : RecyclerView
    private var list = ArrayList<Produk>()

    private val listProduk: ArrayList<Produk>
        get(){
            val dataFoto = resources.getStringArray(R.array.data_foto_produk)
            val dataNama = resources.getStringArray(R.array.data_nama_produk)
            val dataHarga = resources.getIntArray(R.array.data_harga_produk)
            val dataDesc = resources.getStringArray(R.array.data_desc_produk)
            val listProduk = ArrayList<Produk>()
            for (i in dataNama.indices) {
                val produk = Produk(dataFoto[i],dataNama[i],dataHarga[i], dataDesc[i])
                listProduk.add(produk)
            }
            return listProduk
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTukarPointBinding.inflate(layoutInflater)
        setContentView(binding.root)

        list.addAll(listProduk)

        rvProduk = findViewById(R.id.rv_produk)
        rvProduk.setHasFixedSize(true)
        showRecyclerList()

        binding.btnBack.setOnClickListener { this.finish() }
    }

    private fun showRecyclerList() {
        rvProduk.layoutManager = GridLayoutManager(this, 2)
        val listTukarPointAdapter = TukarPointAdapter(list)
        rvProduk.adapter = listTukarPointAdapter

        listTukarPointAdapter.setOnItemClickCallback(object : TukarPointAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Produk) {
                val moveWithObjectIntent = Intent(this@TukarPointActivity, DetailTukarPointActivity::class.java)
                moveWithObjectIntent.putExtra(DetailTukarPointActivity.EXTRA_PRODUK, data)
                startActivity(moveWithObjectIntent)
            }
        })
    }
}