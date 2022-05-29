package com.mbahgojol.chami.ui.main.chat.personal.detail

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.google.firebase.firestore.ktx.toObject
import com.mbahgojol.chami.R
import com.mbahgojol.chami.data.model.*
import com.mbahgojol.chami.databinding.ActivityDetailPersonalChatBinding
import com.mbahgojol.chami.di.FirestoreService
import com.mbahgojol.chami.utils.DateUtils
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class DetailPersonalChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailPersonalChatBinding

    @Inject
    lateinit var service: FirestoreService

    private lateinit var listAdapter: DetailChatAdapter
    private var user: Users? = null
    private var senderId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPersonalChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val model = intent.getParcelableExtra<ChatRoom>("data")
        val isInit = intent.getBooleanExtra("isInit", false)
        senderId = intent.getStringExtra("senderId")
        if (model != null) {
            binding.btnBack.setOnClickListener {
                senderId?.let { service.updateStatusInRoom(it, model.roomid, false) }
                finish()
            }

            if (isInit) service.createRoom(model.roomid)
            senderId?.let {
                service.updateStatusInRoom(it, model.roomid, true)
                service.updateIsReadChat(it, model.roomid, true)
            }

            binding.btnSend.setOnClickListener {
                val currentDate = DateUtils.getCurrentTime()

                val msg = binding.etPesan.text.toString()
                val data = ChatLog(
                    model.user_id,
                    user?.username ?: "anonym",
                    currentDate,
                    0,
                    "",
                    "123",
                    msg,
                    model.roomid
                )

                service.addChat(data, model.roomid)
                    .addOnSuccessListener {
                        service.getRoomChat(model.user_id, model.roomid)
                            .get()
                            .addOnSuccessListener {
                                val chatRoom = it.toObject<ChatRoom>()
                                senderId?.let { it1 ->
                                    service.updateChatDetail(
                                        it1, model.roomid,
                                        Detail(msg, currentDate, true)
                                    )
                                }

                                service.updateChatDetail(
                                    model.user_id, model.roomid,
                                    Detail(msg, currentDate, chatRoom?.inRoom ?: false)
                                )
                            }.addOnFailureListener {
                                Log.e("ChatDetail", it.message.toString())
                            }
                        binding.etPesan.text.clear()
                    }
            }

            listAdapter = DetailChatAdapter(model.user_id) {

            }

            binding.apply {
                rvChat.apply {
                    layoutManager = LinearLayoutManager(this@DetailPersonalChatActivity)
                    adapter = listAdapter
                }

                service.getUserProfile(model.user_id)
                    .addSnapshotListener { snapshot, e ->
                        if (e != null) {
                            Timber.d("Listen failed.")
                            return@addSnapshotListener
                        }

                        if (snapshot != null && snapshot.exists()) {
                            user = snapshot.toObject<Users>()

                            tvName.text = user?.username
                            tvJabatan.text = user?.jabatan
                            avatar.load(user?.profile_url) {
                                transformations(CircleCropTransformation())
                            }

                            if (user?.isonline == true) {
                                binding.isOnline.backgroundTintList =
                                    ContextCompat.getColorStateList(
                                        binding.root.context,
                                        R.color.green
                                    )
                            } else {
                                binding.isOnline.backgroundTintList =
                                    ContextCompat.getColorStateList(
                                        binding.root.context,
                                        R.color.grey
                                    )
                            }
                        } else {
                            Timber.e("Tidak ada List Chat")
                        }
                    }
            }

            listenChat(model.roomid)
        }
    }

    private fun listenChat(roomid: String) {
        service.getChat(roomid)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Timber.d("Listen failed.")
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val model = snapshot.toObject<GetChatResponse>()
                    model?.chatlog?.let { listAdapter.setData(it) }
                } else {
                    Timber.e("Tidak ada List Chat")
                }
            }
    }
}