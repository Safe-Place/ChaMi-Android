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
    private val senderId by lazy { sharedPref.userId }
    private var roomId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

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
        if (intent.hasExtra("data") && model != null) {
            binding.btnAttach.setOnClickListener {
                AttachBottomSheetDialog.newInstance(senderId, model.receiver_id, model.roomid)
                    .apply {
                        show(supportFragmentManager, "")
                    }
            }

            binding.btnBack.setOnClickListener {
                service.updateStatusInRoom(senderId, model.roomid, false)
                finish()
            }

            service.updateStatusInRoom(senderId, model.roomid, true)
            service.updateIsReadChat(senderId, model.roomid, true)

            binding.btnSend.setOnClickListener {
                val currentDate = DateUtils.getCurrentTime()

                val msg = binding.etPesan.text.toString()
                val data = ChatLog(
                    senderId,
                    sharedPref.userName,
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
                                service.updateChatDetail(
                                    senderId, model.roomid,
                                    Detail(msg, currentDate, true)
                                )

                                service.updateChatDetail(
                                    model.receiver_id, model.roomid,
                                    Detail(msg, currentDate, chatRoom?.inRoom ?: false)
                                )

                                service.updateLastUpdateRoom(
                                    senderId,
                                    user?.user_id ?: "",
                                    model.roomid
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
                        binding.rvChat.smoothScrollToPosition(listAdapter.itemCount)
                    }

                binding.rvChat.smoothScrollToPosition(listAdapter.itemCount)
                binding.etPesan.text.clear()
            }

            listAdapter = DetailChatAdapter(senderId) {

            }
            binding.rvChat.adapter = listAdapter

            fetchProfile(model.receiver_id)
            listenChat(model.roomid)
        } else {
            val user = intent.getParcelableExtra<Users>("users")
            val roomId = senderId
                .plus("|")
                .plus(user?.user_id)

            binding.btnAttach.setOnClickListener {
                AttachBottomSheetDialog.newInstance(senderId, user?.user_id ?: "", roomId)
                    .apply {
                        show(supportFragmentManager, "")
                    }
            }

            binding.btnBack.setOnClickListener {
                service.updateStatusInRoom(senderId, roomId, false)
                finish()
            }

            service.updateStatusInRoom(senderId, roomId, true)
            service.updateIsReadChat(senderId, roomId, true)

            service.createRoom(roomId)

            listAdapter = DetailChatAdapter(senderId) {

            }
            binding.rvChat.adapter = listAdapter

            user?.user_id?.let { fetchProfile(it) }
            listenChat(roomId)

            binding.btnSend.setOnClickListener {
                val currentDate = DateUtils.getCurrentTime()

                val msg = binding.etPesan.text.toString()
                val data = ChatLog(
                    senderId,
                    sharedPref.userName,
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
                                service.updateChatDetail(
                                    senderId, roomId,
                                    Detail(msg, currentDate, true)
                                )

                                service.updateChatDetail(
                                    user?.user_id ?: "", roomId,
                                    Detail(msg, currentDate, chatRoom?.inRoom ?: false)
                                )

                                service.updateChatList(
                                    senderId,
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
                        binding.rvChat.smoothScrollToPosition(listAdapter.itemCount)
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
                        binding.rvChat.smoothScrollToPosition(listAdapter.itemCount)
                    }
                } else {
                    Timber.e("Tidak ada List Chat")
                }
            }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        roomId?.let { it1 -> service.updateStatusInRoom(senderId, it1, false) }
    }
}