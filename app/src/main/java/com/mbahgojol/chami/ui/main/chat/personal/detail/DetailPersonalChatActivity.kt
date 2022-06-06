package com.mbahgojol.chami.ui.main.chat.personal.detail

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.google.firebase.firestore.ktx.toObject
import com.mbahgojol.chami.R
import com.mbahgojol.chami.data.SharedPref
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
    private val viewModel by viewModels<DetailPersonalChatViewModel>()

    @Inject
    lateinit var service: FirestoreService

    @Inject
    lateinit var sharedPref: SharedPref

    private lateinit var listAdapter: DetailChatAdapter
    private var user: Users? = null
    private var senderId: String? = null
    private var roomId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)

        binding = ActivityDetailPersonalChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            rvChat.apply {
                val myLayoutManager = LinearLayoutManager(this@DetailPersonalChatActivity)
                layoutManager = myLayoutManager
                myLayoutManager.stackFromEnd = true
            }
        }

        val model = intent.getParcelableExtra<ChatRoom>("data")
        senderId = intent.getStringExtra("senderId")
        if (intent.hasExtra("data") && model != null) {
            binding.btnBack.setOnClickListener {
                senderId?.let { service.updateStatusInRoom(it, model.roomid, false) }
                finish()
            }

            senderId?.let {
                service.updateStatusInRoom(it, model.roomid, true)
                service.updateIsReadChat(it, model.roomid, true)
            }

            binding.btnSend.setOnClickListener {
                val currentDate = DateUtils.getCurrentTime()

                val msg = binding.etPesan.text.toString()
                val data = ChatLog(
                    model.receiver_id,
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
                        service.getRoomChat(model.receiver_id, model.roomid)
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
                                    model.receiver_id, model.roomid,
                                    Detail(msg, currentDate, chatRoom?.inRoom ?: false)
                                )

                                val payload = PayloadNotif(
                                    to = user?.token,
                                    data = PayloadNotif.Data(
                                        model.receiver_id,
                                        senderId,
                                        "",
                                        "",
                                        ""
                                    )
                                )
                                viewModel.sendNotif(payload)
                            }.addOnFailureListener {
                                Log.e("ChatDetail", it.message.toString())
                            }
                    }

                binding.rvChat.smoothScrollToPosition(listAdapter.itemCount)
                binding.etPesan.text.clear()
            }

            listAdapter = DetailChatAdapter(model.receiver_id) {

            }
            binding.rvChat.adapter = listAdapter

            fetchProfile(model.receiver_id)
            listenChat(model.roomid)
        } else {
            val user = intent.getParcelableExtra<Users>("users")
            val roomId = sharedPref.userId
                .plus("|")
                .plus(user?.user_id)

            binding.btnBack.setOnClickListener {
                senderId?.let { service.updateStatusInRoom(it, roomId, false) }
                finish()
            }
            senderId?.let {
                service.updateStatusInRoom(it, roomId, true)
                service.updateIsReadChat(it, roomId, true)
            }

            service.createRoom(roomId)

            listAdapter = DetailChatAdapter(user?.user_id ?: "") {

            }
            binding.rvChat.adapter = listAdapter

            user?.user_id?.let { fetchProfile(it) }
            listenChat(roomId)

            binding.btnSend.setOnClickListener {
                val currentDate = DateUtils.getCurrentTime()

                val msg = binding.etPesan.text.toString()
                val data = ChatLog(
                    user?.user_id ?: "",
                    user?.username ?: "anonym",
                    currentDate,
                    0,
                    "",
                    "123",
                    msg,
                    roomId
                )

                service.addChat(data, roomId)
                    .addOnSuccessListener {
                        service.getRoomChat(user?.user_id ?: "", roomId)
                            .get()
                            .addOnSuccessListener {
                                val chatRoom = it.toObject<ChatRoom>()
                                senderId?.let { it1 ->
                                    service.updateChatDetail(
                                        it1, roomId,
                                        Detail(msg, currentDate, true)
                                    )
                                }

                                service.updateChatDetail(
                                    user?.user_id ?: "", roomId,
                                    Detail(msg, currentDate, chatRoom?.inRoom ?: false)
                                )

                                service.updateChatList(
                                    senderId ?: "",
                                    user?.user_id ?: "",
                                    roomId,
                                    chatRoom?.inRoom ?: false
                                )

                                val payload = PayloadNotif(
                                    to = user?.token,
                                    data = PayloadNotif.Data(
                                        user?.user_id,
                                        senderId,
                                        "",
                                        "",
                                        ""
                                    )
                                )
                                viewModel.sendNotif(payload)
                            }.addOnFailureListener {
                                Log.e("ChatDetail", it.message.toString())
                            }
                    }

                binding.rvChat.smoothScrollToPosition(listAdapter.itemCount)
                binding.etPesan.text.clear()
            }
        }
    }

    private fun fetchProfile(userId: String) {
        service.getUserProfile(userId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Timber.d("Listen failed.")
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    user = snapshot.toObject<Users>()

                    binding.tvName.text = user?.username
                    binding.tvJabatan.text = user?.jabatan
                    binding.avatar.load(user?.profile_url) {
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

    private fun listenChat(roomid: String) {
        this.roomId = roomid
        service.getChat(roomid)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Timber.d("Listen failed.")
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val model = snapshot.toObject<GetChatResponse>()
                    model?.chatlog?.let {
                        listAdapter.setData(it)
                    }
                } else {
                    Timber.e("Tidak ada List Chat")
                }
            }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        senderId?.let { roomId?.let { it1 -> service.updateStatusInRoom(it, it1, false) } }
    }
}