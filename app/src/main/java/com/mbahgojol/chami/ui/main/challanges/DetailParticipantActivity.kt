package com.mbahgojol.chami.ui.main.challanges

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import coil.load
import coil.transform.CircleCropTransformation
import com.mbahgojol.chami.LoginPref
import com.mbahgojol.chami.R
import com.mbahgojol.chami.data.model.Peserta
import com.mbahgojol.chami.data.model.Submission
import com.mbahgojol.chami.data.remote.FirestoreService
import com.mbahgojol.chami.databinding.ActivityDetailParticipantBinding
import com.mbahgojol.chami.dummyData.Challenge
import com.mbahgojol.chami.dummyData.Participant
import com.mbahgojol.chami.dummyData.Produk
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject
import com.mbahgojol.chami.utils.BaseActivity

@AndroidEntryPoint
class DetailParticipantActivity : BaseActivity() {
    private lateinit var binding : ActivityDetailParticipantBinding

    @Inject
    lateinit var firestoreModule: FirestoreService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailParticipantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.getParcelableExtra<Peserta>(EXTRA_PARTICIPANT) as Peserta
        val challengeId = data.challenges_id
        val userId = data.user_id
        firestoreModule.getOneSubmission(challengeId, userId)
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Timber.d("Listen failed.")
//                    binding.progressBar.isVisible = false
                    return@addSnapshotListener
                }
                var submission = Submission()
                for (doc in value!!) {
                    var urlFile = doc.getString("url_file")
                    var namaFile = doc.getString("nama_file")
                    var avatar = doc.getString("avatar_user")
                    var namaUser = doc.getString("nama_user")
                    var userId = doc.getString("user_id")
                    var pesan = doc.getString("pesan")
                    var subId = doc.getString("submission_id")
                    var challengeId = doc.getString("challenge_id")
                    submission = Submission(urlFile,namaFile,avatar,namaUser,userId,pesan,subId,challengeId)
//                    binding.progressBar.isVisible = false
                }
                binding.tvNamaPeserta.text = submission.nama_user
                binding.tvFileSub.text = submission.nama_file
                binding.ivPeserta.load(submission.avatar_user){
                    transformations(CircleCropTransformation())
                }
                if (submission.pesan == "") {
                    submission.pesan = "Tidak ada pesan"
                }
                binding.tvPesanPribadi.text = submission.pesan
            }

    }
    companion object{
        const val EXTRA_PARTICIPANT = "extra participant"
    }
}