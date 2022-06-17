package com.mbahgojol.chami.ui.main.challanges

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.Toast
import androidx.core.view.isVisible
import com.mbahgojol.chami.LoginPref
import com.mbahgojol.chami.R
import com.mbahgojol.chami.data.model.Challenges
import com.mbahgojol.chami.data.model.Files
import com.mbahgojol.chami.data.remote.FirestoreService
import com.mbahgojol.chami.databinding.ActivityTambahChallengeBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class TambahChallengeActivity : AppCompatActivity() {

    @Inject
    lateinit var firestoreModule: FirestoreService

    private lateinit var binding : ActivityTambahChallengeBinding
    private var tenggat : Long? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahChallengeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            this.finishAndRemoveTask()
        }

        val today = Calendar.getInstance()
        binding.spinnerTenggat.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        ) { view, year, month, day ->
            val month = month + 1
            val tenggatString = "$day-$month-$year"
            tenggat = convertDateToLong(tenggatString)

        }

        binding.btnSimpan.setOnClickListener {
            createToFirestore()
            Toast.makeText(this@TambahChallengeActivity, "Challenge ditambahkan", Toast.LENGTH_LONG).show()
            this.finishAndRemoveTask()
        }

    }

    private fun createToFirestore(){
        val judul = binding.etJudulChallenge.text.toString()
        val deskripsi = binding.etDeskripsiChallenge.text.toString()
        val point = binding.etPointChallenge.text
        val user_id = LoginPref(this).getId()
        val date = Calendar.getInstance().time

        val sdf = SimpleDateFormat("dd-MM-yyyy")
        val todayString = sdf.format(date)
        val today = convertDateToLong(todayString)

        val challenge = Challenges(
            judul,
            deskripsi,
            point.toString(),
            tenggat,
            ownerId = user_id,
        )
        firestoreModule.addChallenge(challenge){
        }


    }

    fun convertDateToLong(date: String): Long {
        val df = SimpleDateFormat("dd-MM-yyyy")
        return df.parse(date).time
    }
}