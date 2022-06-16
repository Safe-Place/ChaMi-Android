package com.mbahgojol.chami.ui.main.chat.personal.converse

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mbahgojol.chami.data.model.ChatLog
import com.mbahgojol.chami.data.remote.FirestoreService
import com.mbahgojol.chami.databinding.LayoutAttachBottomBinding
import com.mbahgojol.chami.utils.DateUtils
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AttachBottomSheetDialog : BottomSheetDialogFragment() {

    private var binding: LayoutAttachBottomBinding? = null

    @Inject
    lateinit var service: FirestoreService
    private var senderId = ""
    private var receiverId = ""
    private var roomId = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutAttachBottomBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            btnAttach.setOnClickListener {
                val data = ChatLog(
                    receiverId,
                    "anonym",
                    DateUtils.getCurrentTime(),
                    5,
                    "",
                    UUID.randomUUID().toString(),
                    "File",
                    roomId
                )

                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = "*/*"
                resultFile.launch(intent)
            }
        }
    }

    private var resultFile =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
            }
        }

    private fun uploadtoStorage(uri: Uri) {
        /* val storageRef = storage.reference
         val path : String = "files/"+UUID.randomUUID()
         val filesRef = storageRef.child(path)
         val uploadFile = filesRef.putFile(uri)*/


    }

    companion object {
        fun newInstance(senderId: String, receiverId: String, roomId: String) =
            AttachBottomSheetDialog().apply {
                this.senderId = senderId
                this.receiverId = receiverId
                this.roomId = roomId
            }
    }
}