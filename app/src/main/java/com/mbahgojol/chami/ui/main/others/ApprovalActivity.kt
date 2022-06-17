package com.mbahgojol.chami.ui.main.others

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.mbahgojol.chami.R
import com.mbahgojol.chami.databinding.ActivityApprovalBinding
import com.mbahgojol.chami.databinding.ActivityListParticipantBinding
import com.mbahgojol.chami.dummyData.Participant
import com.mbahgojol.chami.dummyData.Validasi
import com.mbahgojol.chami.ui.main.challanges.DetailParticipantActivity
import com.mbahgojol.chami.ui.main.challanges.ListParticipantAdapter
import com.mbahgojol.chami.utils.BaseActivity

class ApprovalActivity : BaseActivity() {
    private lateinit var binding : ActivityApprovalBinding

    private lateinit var rvValidasi : RecyclerView
    private var list = ArrayList<Validasi>()

    private val listValidasi: ArrayList<Validasi>
        get(){
            val dataAvatar = resources.getStringArray(R.array.data_validasi_avatar)
            val dataNama = resources.getStringArray(R.array.data_validasi_nama)
            val dataDivisi = resources.getStringArray(R.array.data_validasi_divisi)
            val dataDeskripsi = resources.getStringArray(R.array.data_validasi_desc)
            val dataId = resources.getStringArray(R.array.data_validasi_id)
            val listValidasi = ArrayList<Validasi>()
            for (i in dataNama.indices) {
                val validasi = Validasi(dataAvatar[i],dataNama[i],dataDivisi[i], dataDeskripsi[i],dataId[i])
                listValidasi.add(validasi)
            }
            return listValidasi
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApprovalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        list.addAll(listValidasi)

        rvValidasi = findViewById(R.id.rv_validasi)
        rvValidasi.setHasFixedSize(true)
        showRecyclerList()

        binding.btnBack.setOnClickListener { this.finish() }
    }

    private fun showRecyclerList() {
        rvValidasi.layoutManager = LinearLayoutManager(this)
        val listValidasiAdapter = ApprovalAdapter(list)
        rvValidasi.adapter = listValidasiAdapter

        listValidasiAdapter.setOnItemClickCallback(object : ApprovalAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Validasi) {
                val moveWithObjectIntent = Intent(this@ApprovalActivity, DetailAprovalActivity::class.java)
                moveWithObjectIntent.putExtra(DetailAprovalActivity.EXTRA_VALIDASI, data)
                startActivity(moveWithObjectIntent)
            }
        })
    }
}