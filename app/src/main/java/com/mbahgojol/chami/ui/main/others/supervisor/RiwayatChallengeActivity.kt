package com.mbahgojol.chami.ui.main.others.supervisor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mbahgojol.chami.R
import com.mbahgojol.chami.databinding.ActivityRiwayatChallengeBinding
import com.mbahgojol.chami.dummyData.Challenge
import com.mbahgojol.chami.dummyData.HistoryChallenge
import com.mbahgojol.chami.dummyData.RiwayatChallenge

class RiwayatChallengeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRiwayatChallengeBinding
    private lateinit var rvChallenge : RecyclerView
    private var list = ArrayList<RiwayatChallenge>()

    private val listChallenge: ArrayList<RiwayatChallenge>
        get() {
            val dataJudul = resources.getStringArray(R.array.data_judul)
            val dataDeskripsi = resources.getStringArray(R.array.data_deskripsi)
            val dataReward = resources.getIntArray(R.array.data_reward)
            val dataDueData = resources.getStringArray(R.array.due_date)
            val listChallenge = ArrayList<RiwayatChallenge>()
            for (i in dataJudul.indices) {
                val riwayatChallenge = RiwayatChallenge(
                    dataJudul[i], dataDeskripsi[i], dataReward[i],
                    dataDueData[i]
                )
                listChallenge.add(riwayatChallenge)
            }
            return listChallenge
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRiwayatChallengeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            this.finish()
        }

        list.addAll(listChallenge)

        rvChallenge = findViewById(R.id.rv_RiwayatChallenge)
        rvChallenge.setHasFixedSize(true)
        showRecyclerList()
    }

    private fun showRecyclerList(){
        rvChallenge.layoutManager = LinearLayoutManager(this)
        val listChallengeAdapter = RiwayatChallengeAdapter(list)
        rvChallenge.adapter = listChallengeAdapter

//        listHeroAdapter.setOnItemClickCallback(object : ListChallengeAdapter.OnItemClickCallback {
//            override fun onItemClicked(data: Challenge) {
//                val moveWithObjectIntent = Intent(requireActivity(), DetailChallengeFragment::class.java)
//                moveWithObjectIntent.putExtra(DetailChallenge.EXTRA_Challenge, data)
//                startActivity(moveWithObjectIntent)
//            }
//        })
    }

}