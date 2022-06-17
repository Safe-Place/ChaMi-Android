package com.mbahgojol.chami.ui.main.others

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mbahgojol.chami.R
import com.mbahgojol.chami.databinding.ActivityDaftarTransaksiBinding
import com.mbahgojol.chami.dummyData.RiwayatTransaksi
import com.mbahgojol.chami.utils.BaseActivity

class DaftarTransaksiActivity : BaseActivity() {
    private lateinit var binding : ActivityDaftarTransaksiBinding

    private lateinit var rvTransaksi : RecyclerView
    private var list = ArrayList<RiwayatTransaksi>()

    private val listTransaksi: ArrayList<RiwayatTransaksi>
        get(){
            val dataTanggal = resources.getStringArray(R.array.data_tanggal_trx)
            val dataNama = resources.getStringArray(R.array.data_nama_trx)
            val dataHarga = resources.getIntArray(R.array.data_harga_trx)
            val dataStatus = resources.getStringArray(R.array.data_status_trx)
            val listTransaksi = ArrayList<RiwayatTransaksi>()
            for (i in dataTanggal.indices) {
                val transaksi = RiwayatTransaksi(dataTanggal[i],dataNama[i], dataHarga[i],
                    dataStatus[i])
                listTransaksi.add(transaksi)
            }
            return listTransaksi
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaftarTransaksiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        list.addAll(listTransaksi)

        rvTransaksi = findViewById(R.id.rv_transaksi)
        binding.rvTransaksi.setHasFixedSize(true)
        showRecyclerList()

        binding.btnBack.setOnClickListener { this.finish() }
    }

    private fun showRecyclerList() {
        rvTransaksi.layoutManager = LinearLayoutManager(this)
        val listTransaksiAdapter = DaftarTransaksiAdapter(list)
        rvTransaksi.adapter = listTransaksiAdapter
    }
}