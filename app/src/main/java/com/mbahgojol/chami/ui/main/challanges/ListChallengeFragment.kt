package com.mbahgojol.chami.ui.main.challanges

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mbahgojol.chami.R
import com.mbahgojol.chami.dummyData.Challenge
import com.mbahgojol.chami.ui.main.chat.SectionsPagerAdapter

class ListChallengeFragment : Fragment() {

    private lateinit var rvChallenge : RecyclerView
    private var list = ArrayList<Challenge>()

    private val listChallenge: ArrayList<Challenge>
    get(){
        val dataJudul = resources.getStringArray(R.array.data_judul)
        val dataDeskripsi = resources.getStringArray(R.array.data_deskripsi)
        val dataReward = resources.getIntArray(R.array.data_reward)
        val dataDueData = resources.getStringArray(R.array.due_date)
        val dataStatus = resources.getStringArray(R.array.data_status)
        val listChallenge = ArrayList<Challenge>()
        for (i in dataJudul.indices) {
            val challenge = Challenge(dataJudul[i],dataDeskripsi[i], dataReward[i],
                dataDueData[i],dataStatus[i])
            listChallenge.add(challenge)
        }
        return listChallenge
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        list.addAll(listChallenge)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_challenge, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val fragmentView = requireNotNull(view) {"View should not be null when calling onActivityCreated"}

        rvChallenge = fragmentView.findViewById(R.id.rv_Challenge)
        rvChallenge.setHasFixedSize(true)
        showRecyclerList()
    }

    private fun showRecyclerList(){
        rvChallenge.layoutManager = LinearLayoutManager(requireActivity())
        val listChallengeAdapter = ListChallengeAdapter(list)
        rvChallenge.adapter = listChallengeAdapter

        listChallengeAdapter.setOnItemClickCallback(object : ListChallengeAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Challenge) {
                val moveWithObjectIntent = Intent(requireActivity(), DetailChallengeActivity::class.java)
                moveWithObjectIntent.putExtra(DetailChallengeActivity.EXTRA_CHALLENGE, data)
                startActivity(moveWithObjectIntent)
            }
        })
    }
}