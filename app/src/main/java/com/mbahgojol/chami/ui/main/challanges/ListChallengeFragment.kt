package com.mbahgojol.chami.ui.main.challanges

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.toObjects
import com.mbahgojol.chami.R
import com.mbahgojol.chami.data.model.Challenges
import com.mbahgojol.chami.data.model.Files
import com.mbahgojol.chami.data.model.Users
import com.mbahgojol.chami.data.remote.FirestoreService
import com.mbahgojol.chami.databinding.FragmentFilesBinding
import com.mbahgojol.chami.databinding.FragmentListChallengeBinding
import com.mbahgojol.chami.dummyData.Challenge
import com.mbahgojol.chami.ui.main.chat.SectionsPagerAdapter
import com.mbahgojol.chami.ui.main.files.DetailFileActivity
import com.mbahgojol.chami.ui.main.files.FileAdapter
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class ListChallengeFragment : Fragment() {

    private lateinit var binding: FragmentListChallengeBinding

    @Inject
    lateinit var firestoreModule: FirestoreService

    private val listAdapter by lazy {
        ListChallengeAdapter {
            Intent(requireActivity(), DetailChallengeActivity::class.java).apply {
                putExtra(DetailChallengeActivity.EXTRA_CHALLENGE, it)
                startActivity(this)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListChallengeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            rvChallenge.apply {
                layoutManager = LinearLayoutManager(requireActivity())
                adapter = listAdapter
                setHasFixedSize(true)
            }
        }

        val date = Calendar.getInstance().time
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        val todayString = sdf.format(date)
        val today = convertDateToLong(todayString)

        firestoreModule.getListChallenge(today)
            .get()
            .addOnSuccessListener { documents ->
                var listChallenge = ArrayList<Challenges>()
                for (doc in documents) {
                    var judul = doc.getString("judul")
                    var deskripsi = doc.getString("deskripsi")
                    var point = doc.getString("point")
                    var tenggat = doc.getLong("due_date")
                    var challenges_id = doc.getString("challenge_id")
                    var owner_id = doc.getString("ownerId")
                    var challenge = Challenges(judul, deskripsi,point,tenggat,challenges_id,owner_id)
                    listChallenge.add(challenge)
                    Log.d(TAG, "${doc.id} => ${doc.data}")
                }
                listAdapter.setData(listChallenge)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents files: ", exception)
            }
    }

    fun convertDateToLong(date: String): Long {
        val df = SimpleDateFormat("dd-MM-yyy")
        return df.parse(date).time
    }

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("dd-MM-yyyy")
        return format.format(date)
    }
}