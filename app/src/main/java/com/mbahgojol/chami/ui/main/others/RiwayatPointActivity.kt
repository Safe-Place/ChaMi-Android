package com.mbahgojol.chami.ui.main.others

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mbahgojol.chami.R
import com.mbahgojol.chami.databinding.ActivityDaftarTransaksiBinding
import com.mbahgojol.chami.databinding.ActivityRiwayatPointBinding
import com.mbahgojol.chami.dummyData.RiwayatPoint
import com.mbahgojol.chami.dummyData.RiwayatTransaksi
import com.mbahgojol.chami.utils.BaseActivity

class RiwayatPointActivity : BaseActivity() {
    private lateinit var binding : ActivityRiwayatPointBinding

    private lateinit var rvPoint : RecyclerView
    private var list = ArrayList<RiwayatPoint>()

    private val listPoint: ArrayList<RiwayatPoint>
        get(){
            val dataJenis = resources.getStringArray(R.array.data_jenis_riwayat)
            val dataNama = resources.getStringArray(R.array.data_nama_riwayat)
            val dataTanggal = resources.getStringArray(R.array.data_tanggal_riwayat)
            val dataNominal = resources.getIntArray(R.array.data_nominal_riwayat)
            val dataStatus = resources.getStringArray(R.array.data_status_riwayat)
            val listPoint = ArrayList<RiwayatPoint>()
            for (i in dataNama.indices) {
                val point = RiwayatPoint(dataJenis[i],dataNama[i],dataTanggal[i], dataNominal[i],
                    dataStatus[i])
                listPoint.add(point)
            }
            return listPoint
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRiwayatPointBinding.inflate(layoutInflater)
        setContentView(binding.root)

        list.addAll(listPoint)

        rvPoint = findViewById(R.id.rv_RiwayatPoint)
        rvPoint.setHasFixedSize(true)
        showRecyclerList()

        binding.btnBack.setOnClickListener { this.finish() }
    }

    private fun showRecyclerList() {
        rvPoint.layoutManager = LinearLayoutManager(this)
        val listRiwayatPointAdapter = RiwayatPointAdapter(list)
        rvPoint.adapter = listRiwayatPointAdapter
    }
}