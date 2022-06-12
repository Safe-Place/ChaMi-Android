package com.mbahgojol.chami.ui.main.others

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import coil.load
import coil.transform.CircleCropTransformation
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.mbahgojol.chami.LoginPref
import com.mbahgojol.chami.R
import com.mbahgojol.chami.data.SharedPref
import com.mbahgojol.chami.data.remote.FirestoreService
import com.mbahgojol.chami.databinding.ActivityEditDataBinding
import com.mbahgojol.chami.databinding.ActivityLoginBinding
import com.mbahgojol.chami.signup.uriToFile
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.io.File
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class EditDataActivity : AppCompatActivity() {
    private lateinit var binding : ActivityEditDataBinding

    val storage = Firebase.storage("gs://chami-dev-8390a.appspot.com")

    @Inject
    lateinit var service: FirestoreService

    @Inject
    lateinit var sharedPref: SharedPref

    private var getAvatar: File? = null
    private var uriAvatar: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setProfile()

        binding.tvGantiAvatar.setOnClickListener{ startGallery() }
        binding.btnSave.setOnClickListener { saveProfil() }
        binding.btnBack.setOnClickListener { this.finish() }
    }

    private fun setProfile(){
        val pref = LoginPref(this)

        val nama = pref.getNama()
        val email = pref.getEmail()
        val divisi = pref.getDivisi()
        val id = pref.getId()
        val avatar = pref.getAvatar()

        binding.nama.text = nama.toString().toEditable()
        binding.email.text = email.toString().toEditable()
        binding.divisi.text = divisi.toString().toEditable()
        binding.idPegawai.text = id.toString().toEditable()
        binding.avatar.load(avatar) {
            transformations(CircleCropTransformation())
        }
    }

    private fun saveProfil(){
        val namaBaru = binding.nama.text.toString()
        val divisiBaru = binding.divisi.text.toString()

        when{
            namaBaru.isEmpty() -> {
                binding.nama.error = "Tidak boleh kosong"
            }
            divisiBaru.isEmpty() -> {
                binding.divisi.error = "Tidak boleh kosong"
            }
        }

        uriAvatar?.let { updateAvatar(it) }

        updateData(namaBaru,divisiBaru)
    }

    private fun updateData(nama: String, divisi: String){
        // update nama ke firestore & preference
        service.getUserFromDocId(sharedPref.userId)
            .update("username",nama)
            .addOnSuccessListener {
                LoginPref(this).setNama(nama)
                Timber.d("Sukses update nama ke firestore") }
            .addOnFailureListener { e -> Timber.tag(TAG).w(e, "Gagal update nama ke firestore") }

        // update divisi ke firestore & preference
        service.getUserFromDocId(sharedPref.userId)
            .update("jabatan",divisi)
            .addOnSuccessListener {
                LoginPref(this).setDivisi(divisi)
                Timber.d("Sukses update divisi ke firestore")
                val intent = Intent(this, OthersFragment::class.java)
                startActivity(intent) }
            .addOnFailureListener { e -> Timber.tag(TAG).w(e, "Gagal update divisi ke firestore") }
    }

    private fun updateAvatar (uri: Uri){
//        binding.progressBar.isVisible = true
        // upload avatar to storage
        val storageRef = storage.reference
        val path : String = "avatar/"+ UUID.randomUUID()
        val filesRef = storageRef.child(path)
        val uploadFile = filesRef.putFile(uri)

        uploadFile
            .addOnFailureListener {
                Timber.e(it.message)
//                binding.progressBar.isVisible = false
            }
            .addOnSuccessListener { taskSnapshot ->
//                Toast.makeText(this, "Upload Berhasil", Toast.LENGTH_LONG).show()
                Timber.e("berhasil upload avatar ke storage")
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

                //update avatar to firestore & preference
                service.getUserFromDocId(sharedPref.userId)
                    .update("profile_url",downloadUri)
                    .addOnSuccessListener {
                        LoginPref(this).setAvatar(downloadUri.toString())
                        Timber.d("Sukses update avatar ke firestore") }
                    .addOnFailureListener { e -> Timber.tag(TAG).w(e, "Gagal update avatar ke firestore") }
            }
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            uriAvatar = result.data?.data as Uri
            val myFile = uriToFile(uriAvatar!!, this@EditDataActivity)
            getAvatar = myFile
            binding.avatar.load(getAvatar) {
                transformations(CircleCropTransformation())
            }
        }
    }

    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)
}