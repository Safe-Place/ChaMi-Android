package com.mbahgojol.chami.ui.main.chat.personal.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.google.firebase.firestore.ktx.toObject
import com.mbahgojol.chami.R
import com.mbahgojol.chami.data.model.ChatLog
import com.mbahgojol.chami.data.model.ChatModel
import com.mbahgojol.chami.data.model.HistoryChatModel
import com.mbahgojol.chami.databinding.ActivityDetailPersonalChatBinding
import com.mbahgojol.chami.di.FirestoreService
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class DetailPersonalChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailPersonalChatBinding

    @Inject
    lateinit var firestoreModule: FirestoreService

    private lateinit var listAdapter: DetailChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPersonalChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        val model = intent.getParcelableExtra<HistoryChatModel>("data")
        if (model != null) {
            binding.btnSend.setOnClickListener {
                val data = ChatLog(
                    model.user_id,
                    model.username,
                    "",
                    0,
                    "",
                    "123",
                    binding.etPesan.text.toString()
                )
                firestoreModule.addChatByRoomId(model.roomid, data)
                    .addOnSuccessListener {
                        Timber.e("DocumentSnapshot successfully written!")
                    }
                    .addOnFailureListener { e -> Timber.e("Error writing document", e) }
            }

            listAdapter = DetailChatAdapter(model.user_id) {

            }

            binding.apply {
                rvChat.apply {
                    layoutManager = LinearLayoutManager(this@DetailPersonalChatActivity)
                    adapter = listAdapter
                }

                tvName.text = model.username
                tvJabatan.text = model.jabatan
                avatar.load(model.profile_url) {
                    transformations(CircleCropTransformation())
                }

                if (model.isonline) {
                    binding.isOnline.backgroundTintList =
                        ContextCompat.getColorStateList(binding.root.context, R.color.green)
                } else {
                    binding.isOnline.backgroundTintList =
                        ContextCompat.getColorStateList(binding.root.context, R.color.grey)
                }
            }

            firestoreModule.listenChatByRoomId(model.roomid)
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Timber.d("Listen failed.")
                        return@addSnapshotListener
                    }

                    if (snapshot != null && snapshot.exists()) {
                        val response = snapshot.toObject<ChatModel>()
                        response?.let { listAdapter.setData(it.chatlog) }
                    } else {
                        Timber.e("Tidak ada List Chat")
                    }
                }
        }
    }
}