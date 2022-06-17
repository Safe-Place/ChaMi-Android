package com.mbahgojol.chami.ui.main.challanges

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Telephony
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mbahgojol.chami.R
import com.mbahgojol.chami.databinding.ActivityListParticipantBinding
import com.mbahgojol.chami.databinding.ActivityTukarPointBinding
import com.mbahgojol.chami.dummyData.Participant
import com.mbahgojol.chami.dummyData.Produk
import com.mbahgojol.chami.ui.main.files.DetailFileActivity
import com.mbahgojol.chami.ui.main.others.DetailTukarPointActivity
import com.mbahgojol.chami.ui.main.others.TukarPointAdapter
import com.mbahgojol.chami.utils.BaseActivity

class ListParticipantActivity : BaseActivity() {
    private lateinit var binding : ActivityListParticipantBinding

    private lateinit var rvParticipant : RecyclerView
    private var list = ArrayList<Participant>()

    private val listParticipant: ArrayList<Participant>
        get(){
            val dataFoto = resources.getStringArray(R.array.data_participant_avatar)
            val dataNama = resources.getStringArray(R.array.data_participant_nama)
            val dataFile = resources.getStringArray(R.array.data_participant_file)
            val dataPesan = resources.getStringArray(R.array.data_participant_pesan)
            val listParticipant = ArrayList<Participant>()
            for (i in dataNama.indices) {
                val participant = Participant(dataFoto[i],dataNama[i],dataFile[i], dataPesan[i])
                listParticipant.add(participant)
            }
            return listParticipant
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListParticipantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        list.addAll(listParticipant)

        rvParticipant = findViewById(R.id.rv_Participant)
        rvParticipant.setHasFixedSize(true)
        showRecyclerList()

        binding.btnJadiPemenang.setOnClickListener {
            finishAndRemoveTask()
        }

        binding.btnBack.setOnClickListener { this.finish() }
    }

    private fun showRecyclerList() {
        rvParticipant.layoutManager = LinearLayoutManager(this)
        val listParticipantAdapter = ListParticipantAdapter(list)
        rvParticipant.adapter = listParticipantAdapter

        listParticipantAdapter.setOnItemClickCallback(object : ListParticipantAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Participant) {
                val moveWithObjectIntent = Intent(this@ListParticipantActivity, DetailParticipantActivity::class.java)
                    moveWithObjectIntent.putExtra(DetailParticipantActivity.EXTRA_PARTICIPANT, data)
                startActivity(moveWithObjectIntent)
            }
        })
    }
}