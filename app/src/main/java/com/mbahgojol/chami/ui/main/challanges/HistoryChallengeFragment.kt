package com.mbahgojol.chami.ui.main.challanges

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mbahgojol.chami.R
import com.mbahgojol.chami.dummyData.HistoryChallenge

class HistoryChallengeFragment : Fragment() {

    private lateinit var rvHistoryChallenge : RecyclerView
    private var list = ArrayList<HistoryChallenge>()

    private val listHistoryChallenge: ArrayList<HistoryChallenge>
        get(){
            val dataJudul = resources.getStringArray(R.array.data_ric_judul)
            val dataDeskripsi = resources.getStringArray(R.array.data_ric_desc)
            val dataReward = resources.getIntArray(R.array.data_ric_reward)
            val dataDueData = resources.getStringArray(R.array.data_ric_tenggat)
            val dataStatus = resources.getStringArray(R.array.data_ric_status)
            val listHistoryChallenge = ArrayList<HistoryChallenge>()
            for (i in dataJudul.indices) {
                val historyChallenge = HistoryChallenge(dataJudul[i],dataDeskripsi[i], dataReward[i],
                    dataDueData[i],dataStatus[i])
                listHistoryChallenge.add(historyChallenge)
            }
            return listHistoryChallenge
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        list.addAll(listHistoryChallenge)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history_challenge, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        rvHistoryChallenge = view.findViewById(R.id.rv_historyChallenge)
        rvHistoryChallenge.setHasFixedSize(true)
        showRecyclerList()
    }


    private fun showRecyclerList(){
        rvHistoryChallenge.layoutManager = LinearLayoutManager(requireActivity())
        val listHistoryChallengeAdapter = HistoryChallengeAdapter(list)
        rvHistoryChallenge.adapter = listHistoryChallengeAdapter

//        listHeroAdapter.setOnItemClickCallback(object : ListChallengeAdapter.OnItemClickCallback {
//            override fun onItemClicked(data: Challenge) {
//                val moveWithObjectIntent = Intent(requireActivity(), DetailChallengeFragment::class.java)
//                moveWithObjectIntent.putExtra(DetailChallenge.EXTRA_Challenge, data)
//                startActivity(moveWithObjectIntent)
//            }
//        })
    }
}