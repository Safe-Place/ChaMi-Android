package com.mbahgojol.chami.ui.main.files

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.mbahgojol.chami.LoginPref
import com.mbahgojol.chami.data.SharedPref
import com.mbahgojol.chami.data.model.Files
import com.mbahgojol.chami.data.model.Users
import com.mbahgojol.chami.data.remote.FirestoreService
import com.mbahgojol.chami.databinding.FragmentFilesBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class FilesFragment : Fragment() {

    private lateinit var binding: FragmentFilesBinding

    @Inject
    lateinit var firestoreModule: FirestoreService
    @Inject
    lateinit var sharedPref: SharedPref

    val storage = Firebase.storage("gs://chami-dev-8390a.appspot.com")
    lateinit var uri: Uri

    private val listAdapter by lazy {
        FileAdapter {
            Intent(requireActivity(), DetailFileActivity::class.java).apply {
                putExtra("data", it)
                startActivity(this)
            }
//            val pdfUri = Uri.parse(it.file_url)
//            val shareIntent = ShareCompat.IntentBuilder.from(requireActivity())
//                .setText("Share PDF doc")
//                .setType("application/pdf")
//                .setStream(pdfUri)
//                .intent
//                .setPackage("com.google.android.apps.docs")
//            startActivity(shareIntent)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFilesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            rvFiles.apply {
                layoutManager = LinearLayoutManager(requireActivity())
                adapter = listAdapter
                setHasFixedSize(true)
            }
        }

        //get file berdasarkan divisi user
        getDivisi()

        binding.fbAdd.setOnClickListener {

            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
//            intent.type = "application/pdf"
            intent.type = "*/*"
            startActivityForResult(intent, 777)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 777) {
            uri= data!!.data!!
            uploadtoStorage(uri)
        }
    }

    private fun getDivisi(){
        firestoreModule.getUserFromId(LoginPref(requireActivity()).getId())
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Timber.d("Listen failed.")
                    return@addSnapshotListener
                }
                var divisi : String?
                for (doc in value!!) {
                    divisi = doc.getString("jabatan")
                    getfile (divisi)
                }

            }
    }

    fun getfile (user_div : String?){
        firestoreModule.getFiles(user_div)
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Timber.d("Listen failed.")
                    return@addSnapshotListener
                }

                var data = ArrayList<Files>()
                for (doc in value!!) {
                    var nama_file = doc.getString("nama_file")
                    var type = doc.getLong("type")
                    var file_url = doc.getString("file_url")
                    var size_byte = doc.getString("size_byte")
                    var create_at = doc.getString("create_at")
                    var author_id = doc.getString("author_id")
                    var file_id = doc.getString("file_id")
                    var author_div = doc.getString("author_div")

                    var file = Files(nama_file,type,file_url,size_byte,create_at,author_id,file_id,author_div)
                    data.add(file)
                }
                listAdapter.setData(data)
            }
    }

    private fun uploadtoStorage (uri: Uri){
        val storageRef = storage.reference
        val path : String = "files/"+UUID.randomUUID()
        val filesRef = storageRef.child(path)
        val uploadFile = filesRef.putFile(uri)

        uploadFile
            .addOnFailureListener {
                Toast.makeText(requireActivity(), it.message, Toast.LENGTH_LONG).show()
            }
            .addOnSuccessListener { taskSnapshot ->
                Toast.makeText(requireActivity(), "Upload Berhasil", Toast.LENGTH_LONG).show()
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

                    uploadtoFirestore(downloadUri,nameFile,sizeFile,createAt)
                }.addOnFailureListener {
                    Timber.e("Gagal mendapatkan metadata")
                }

            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun uploadtoFirestore(downloadUri:Uri, nameFile:String?, sizeFile:Long, createAt:Long){
        val author_id = LoginPref(requireActivity()).getId()
        val typeFile : Long = 5
        val format = SimpleDateFormat("dd-MM-yyyy HH:mm")
        val createDate = format.format(Date(createAt))

        firestoreModule.getUserFromId(author_id)
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Timber.d("Listen failed.")
                    return@addSnapshotListener
                }
                var divisi : String?
                for (doc in value!!) {
                    divisi = doc.getString("jabatan")
                    val file = Files(
                        nameFile,
                        typeFile,
                        downloadUri.toString(),
                        sizeFile.toInt().toString(),
                        createDate.toString(),
                        author_id,
                        author_div = divisi
                    )
                    firestoreModule.addFile(file){
                    }
                }
            }
    }
}