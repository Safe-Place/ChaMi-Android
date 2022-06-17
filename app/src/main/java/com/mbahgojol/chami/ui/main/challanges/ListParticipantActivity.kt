package com.mbahgojol.chami.ui.main.challanges

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Telephony
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mbahgojol.chami.R
import com.mbahgojol.chami.data.model.Challenges
import com.mbahgojol.chami.data.model.Peserta
import com.mbahgojol.chami.data.remote.FirestoreService
import com.mbahgojol.chami.databinding.ActivityListParticipantBinding
import com.mbahgojol.chami.databinding.ActivityTukarPointBinding
import com.mbahgojol.chami.dummyData.Participant
import com.mbahgojol.chami.dummyData.Produk
import com.mbahgojol.chami.ui.main.files.DetailFileActivity
import com.mbahgojol.chami.ui.main.others.DetailTukarPointActivity
import com.mbahgojol.chami.ui.main.others.TukarPointAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.mbahgojol.chami.utils.BaseActivity

@AndroidEntryPoint
class ListParticipantActivity : BaseActivity() {
    private lateinit var binding : ActivityListParticipantBinding

    private lateinit var rvParticipant : RecyclerView
    private var list = ArrayList<Peserta>()

    @Inject
    lateinit var firestoreModule: FirestoreService

    private val listAdapter by lazy {
        ListParticipantAdapter {
            Intent(this, DetailParticipantActivity::class.java).apply {
                putExtra(DetailParticipantActivity.EXTRA_PARTICIPANT, it)
                startActivity(this)
            }
        }
    }
//
//    private val listParticipant: ArrayList<Participant>
//        get(){
//            val dataFoto = resources.getStringArray(R.array.data_participant_avatar)
//            val dataNama = resources.getStringArray(R.array.data_participant_nama)
//            val dataFile = resources.getStringArray(R.array.data_participant_file)
//            val dataPesan = resources.getStringArray(R.array.data_participant_pesan)
//            val listParticipant = ArrayList<Participant>()
//            for (i in dataNama.indices) {
//                val participant = Participant(dataFoto[i],dataNama[i],dataFile[i], dataPesan[i])
//                listParticipant.add(participant)
//            }
//            return listParticipant
//        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListParticipantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val challengeId = intent.getStringExtra(EXTRA_CHALLENGEID).toString()

        binding.apply {
            rvParticipant.apply {
                layoutManager = LinearLayoutManager(this@ListParticipantActivity)
                adapter = listAdapter
                setHasFixedSize(true)
            }
        }

        firestoreModule.getListPeserta(challengeId)
            .get()
            .addOnSuccessListener { documents ->
                var listPeserta = ArrayList<Peserta>()
                for (doc in documents) {
                    var challengeId = doc.getString("challenges_id")
                    var nama = doc.getString("nama_user")
                    var avatar = doc.getString("avatar_url")
                    var userId = doc.getString("user_id")
                    var isWinner = doc.getBoolean("winner")
                    var peserta = Peserta(challengeId,nama,avatar,userId,isWinner)
                    listPeserta.add(peserta)
                    Log.d(ContentValues.TAG, "${doc.id} => ${doc.data}")
                }
                listAdapter.setData(listPeserta)
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents files: ", exception)
            }
//        list.addAll(listParticipant)
//
//        rvParticipant = findViewById(R.id.rv_Participant)
//        rvParticipant.setHasFixedSize(true)
//        showRecyclerList()

        binding.btnJadiPemenang.setOnClickListener {
            finishAndRemoveTask()
        }

        binding.btnBack.setOnClickListener { this.finish() }
    }

    companion object{
        const val EXTRA_CHALLENGEID = "extra chellenge id"
    }
}