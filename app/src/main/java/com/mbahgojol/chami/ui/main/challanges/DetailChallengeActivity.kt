package com.mbahgojol.chami.ui.main.challanges

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import coil.load
import coil.transform.CircleCropTransformation
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.mbahgojol.chami.LoginPref
import com.mbahgojol.chami.R
import com.mbahgojol.chami.data.model.Challenges
import com.mbahgojol.chami.data.model.Files
import com.mbahgojol.chami.data.model.Peserta
import com.mbahgojol.chami.data.model.Submission
import com.mbahgojol.chami.data.remote.FirestoreService
import com.mbahgojol.chami.databinding.ActivityDetailChallengeBinding
import com.mbahgojol.chami.dummyData.Challenge
import com.mbahgojol.chami.dummyData.Produk
import com.mbahgojol.chami.ui.main.files.DetailFileActivity
import com.mbahgojol.chami.ui.main.others.DetailTukarPointActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class DetailChallengeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailChallengeBinding

    @Inject
    lateinit var firestoreModule: FirestoreService

    val storage = Firebase.storage("gs://chami-dev-8390a.appspot.com")
    lateinit var uri: Uri
    private var challengeId: String? = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailChallengeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.getParcelableExtra<Challenges>(EXTRA_CHALLENGE) as Challenges

        val image = listOf(
            "https://i.pinimg.com/736x/84/6c/72/846c720bbb1ec76af83d290f1b8cc97c.jpg",
            "https://i.pinimg.com/474x/93/1a/8d/931a8d683d3377eb8bf216438959ec1d.jpg",
            "https://i.pinimg.com/564x/18/b9/ff/18b9ffb2a8a791d50213a9d595c4dd52.jpg",
            "https://i.pinimg.com/474x/86/ea/e3/86eae3d8abc2362ad6262916cb950640.jpg",
            "https://i.pinimg.com/474x/33/d7/70/33d770443af698d1dc410a32482fa264.jpg",
            "https://i.pinimg.com/564x/12/6f/3c/126f3c861b4abbb9eb891c21f4fd8740.jpg",
            "https://i.pinimg.com/736x/2f/ec/a4/2feca4c9330929232091f910dbff7f87.jpg",
            "https://i.pinimg.com/originals/f9/ba/90/f9ba90e3eba7af18a0ca139844ed08d5.jpg",
            "https://i.pinimg.com/originals/3f/c3/d2/3fc3d2a90e45cc51b9c2f2ec67992050.png",
            "https://i.pinimg.com/originals/eb/7f/a7/eb7fa775f1ee3ca4f8beeaff5dc9d468.jpg",
        )

        val avatar = image.random()

        challengeId = data.challenge_id

        // cek jabatan user
        val jabatan = LoginPref(this@DetailChallengeActivity).getPosisi()
        if (jabatan != "Agent"){
            binding.linearSubmission.isVisible = false
            binding.btnParticipant.isVisible = true
        } else{
            ceksubmission()
        }

        binding.btnParticipant.setOnClickListener {
            Intent(this, ListParticipantActivity::class.java).apply {
                putExtra(ListParticipantActivity.EXTRA_CHALLENGEID, challengeId)
                startActivity(this)
            }
        }

        val tenggat = convertLongToTime(data.due_date!!)
        binding.pointDetail.text = data.point.toString() +" Point"
        binding.tenggatDetail.text = tenggat
        binding.judulDetail.text = data.judul
        binding.deskDetail.text = data.deskripsi
//        binding.avatarWinner.load(avatar){
//            transformations(CircleCropTransformation())
//        }
        binding.avatarWinner.isInvisible = false
        binding.tvPemenang.isVisible = false

        binding.btnBack.setOnClickListener {
            finishAndRemoveTask()
        }

        binding.btnTambahLampiran.setOnClickListener {
            pilihFile()
        }

        binding.btnTandaiSelesai.setOnClickListener {
            binding.btnTandaiSelesai.background = getDrawable(R.drawable.custom_rounded_button)
            binding.btnTandaiSelesai.setTextColor(ContextCompat.getColor(this,R.color.white))

            binding.btnTambahLampiran.isVisible = false
//            binding.btnBatalSubmit.apply{
//                isVisible = true
//                setOnClickListener {
//                    binding.btnTambahLampiran.isVisible = true
//                    binding.btnBatalSubmit.isVisible = false
//                    binding.btnTandaiSelesai.background = getDrawable(R.drawable.custom_round_stroke_bg)
//                    binding.btnTandaiSelesai.setTextColor(ContextCompat.getColor(this@DetailChallengeActivity,R.color.yellow))
//                }
//            }
        }


        binding.btnKirimPesan.setOnClickListener {
            binding.inputPesan.text = null
            Toast.makeText(this@DetailChallengeActivity, "Pesan Terkirim", Toast.LENGTH_LONG).show()
        }

    }

    private fun pilihFile(){
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
//            intent.type = "application/pdf"
        intent.type = "*/*"
        startActivityForResult(intent, 777)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 777) {
            uri= data!!.data!!
            val nameFile = getFileNameFromUri(this,uri)
            uploadtoStorage(uri, nameFile)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun uploadtoStorage (uri: Uri, nameFile: String?){
        val storageRef = storage.reference
        val path : String = "submission/"+ UUID.randomUUID()+"/"+nameFile
        val filesRef = storageRef.child(path)
        val uploadFile = filesRef.putFile(uri)

        uploadFile
            .addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }
            .addOnSuccessListener { taskSnapshot ->
//                Toast.makeText(this, "Upload Berhasil", Toast.LENGTH_LONG).show()
                Timber.e("sukses upload ke storage")
            }

        uploadFile.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            filesRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result

                filesRef.metadata.addOnSuccessListener { metadata ->
                    val nameFile = metadata.name
                    val sizeFile = metadata.sizeBytes
                    val createAt = metadata.creationTimeMillis

                    binding.btnTambahLampiran.isVisible = false
                    binding.btnTandaiSelesai.isVisible = false
                    binding.tvFile.isVisible = true
                    binding.btnKirim.isVisible = true
                    binding.btnKirim.setOnClickListener {
                        uploadtoFirestore(nameFile, downloadUri)
                    }

                    binding.tvFile.text = nameFile

                }.addOnFailureListener {
                    Timber.e("Gagal mendapatkan metadata")
                }
            }
        }
    }

    private fun uploadtoFirestore(namaFile: String?, fileUri: Uri) {
        val avatar = LoginPref(this).getAvatar()
        val namaUser = LoginPref(this).getNama()
        val idUser = LoginPref(this).getId()

        val submission = Submission(
            fileUri.toString(),
            namaFile,
            avatar,
            namaUser,
            idUser,
            "",
            challenge_id = challengeId
        )

        val peserta = Peserta(
            challengeId,
            namaUser,
            avatar,
            idUser,
            false
        )
//                    binding.progressBar.isVisible = false
        firestoreModule.submitChallenge(submission, challengeId) {
        }
        firestoreModule.addPeserta(peserta, challengeId).addOnSuccessListener {
            Toast.makeText(this, "Submission terkirim", Toast.LENGTH_LONG).show()
            binding.btnKirim.isVisible = false
        }
    }

        @SuppressLint("Range")
        fun getFileNameFromUri(context: Context, uri: Uri): String? {
            val fileName: String?
            val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
            cursor?.moveToFirst()
            fileName = cursor?.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            cursor?.close()
            return fileName
        }

        fun convertLongToTime(time: Long): String {
            val date = Date(time)
            val format = SimpleDateFormat("dd-MM-yyyy")
            return format.format(date)
        }

    fun ceksubmission() {
        var userId = LoginPref(this).getId()
        firestoreModule.getOneSubmission(challengeId, userId)
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Timber.d("Listen failed.")
//                    binding.progressBar.isVisible = false
                    return@addSnapshotListener
                }
                var namaFile: String? = null
                for (doc in value!!) {
                    namaFile = doc.getString("nama_file")
//                    binding.progressBar.isVisible = false
                }
                if (namaFile != null) {
                    binding.btnTambahLampiran.isVisible = false
                    binding.tvFile.isVisible = true
                    binding.tvFile.text = namaFile
                    binding.btnKirim.isVisible = false
                }
            }
    }

    companion object{
        const val EXTRA_CHALLENGE = "extra challenge"
    }
}