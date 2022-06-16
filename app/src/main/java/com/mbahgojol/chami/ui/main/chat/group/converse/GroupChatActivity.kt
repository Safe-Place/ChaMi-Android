package com.mbahgojol.chami.ui.main.chat.group.converse

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.google.firebase.firestore.ktx.toObject
import com.mbahgojol.chami.data.SharedPref
import com.mbahgojol.chami.data.model.ChatLog
import com.mbahgojol.chami.data.model.GetChatResponse
import com.mbahgojol.chami.data.model.GrupChat
import com.mbahgojol.chami.data.model.PayloadNotif
import com.mbahgojol.chami.data.remote.FirestoreService
import com.mbahgojol.chami.databinding.ActivityGroupChatBinding
import com.mbahgojol.chami.ui.main.chat.personal.converse.setStatusBarGradiant
import com.mbahgojol.chami.utils.DateUtils
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*
import java.util.function.Predicate
import javax.inject.Inject

@AndroidEntryPoint
class GroupChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGroupChatBinding

    @Inject
    lateinit var service: FirestoreService

    @Inject
    lateinit var sharedPref: SharedPref

    private val viewModel by viewModels<GroupChatViewModel>()

    private val myAdapter by lazy {
        GroupChatAdapter(sharedPref.userId) {

        }
    }

    private var groupChat: GrupChat? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupChatBinding.inflate(layoutInflater)
        setStatusBarGradiant(this)
        setContentView(binding.root)

        binding.rvChat.apply {
            val myLayoutManager = LinearLayoutManager(this@GroupChatActivity)
            layoutManager = myLayoutManager
            adapter = myAdapter
            myLayoutManager.stackFromEnd = true
        }

        viewModel.userList.observe(this) { list ->
            binding.tvJabatan.text = list.map { it.username }.toString().replace("[", "")
                .replace("]", "")
        }

        if (intent.hasExtra("id")) {
            val id = intent.getStringExtra("id") ?: ""
            service.removeNotifCountGrup(id, sharedPref.userId)

            service.getGroupById(id)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Timber.d("Listen failed.")
                        return@addSnapshotListener
                    }

                    if (snapshot != null && snapshot.exists()) {
                        groupChat = snapshot.toObject<GrupChat>()
                        binding.avatar.load(groupChat?.imgGroup) {
                            transformations(CircleCropTransformation())
                        }
                        binding.tvName.text = groupChat?.namaGroup
                        groupChat?.participants?.let { listUser ->
                            val participan = listUser.toMutableList()
                            participan.remove(sharedPref.userId)
                            viewModel.getAllUser(participan)
                        }
                    }
                }

            service.getChatLog(id)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Timber.d("Listen failed.")
                        return@addSnapshotListener
                    }

                    if (snapshot != null && snapshot.exists()) {
                        val chatlog = snapshot.toObject<GetChatResponse>()
                        chatlog?.chatlog?.let {
                            myAdapter.setData(it)
                            binding.rvChat.smoothScrollToPosition(myAdapter.itemCount)
                        }
                    }
                }

            binding.btnSend.setOnClickListener {
                val msg = binding.etPesan.text.toString()

                val data = ChatLog(
                    sharedPref.userId,
                    sharedPref.userName,
                    DateUtils.getCurrentTime(),
                    0,
                    "",
                    UUID.randomUUID().toString(),
                    msg,
                    groupChat?.id ?: ""
                )

                service.addChatLog(data)
                    .addOnSuccessListener {
                        val payload = PayloadNotif(
                            data = PayloadNotif.Data(
                                "",
                                sharedPref.userId,
                                id,
                                "",
                                ""
                            )
                        )

                        viewModel.addAllCountNotif(groupChat?.participants, id)
                        viewModel.sendNotif(payload)
                        service.updateLastChatGroup(sharedPref.userId, sharedPref.userName, id, msg)
                        binding.rvChat.smoothScrollToPosition(myAdapter.itemCount)
                    }

                binding.rvChat.smoothScrollToPosition(myAdapter.itemCount)
                binding.etPesan.text.clear()
            }

            binding.btnBack.setOnClickListener {
                service.removeNotifCountGrup(id, sharedPref.userId)
                    .addOnSuccessListener {
                        service.changeIsReadGroup(groupChat?.id ?: "", sharedPref.userId, false)
                    }
                finish()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        service.removeNotifCountGrup(groupChat?.id ?: "", sharedPref.userId)
            .addOnSuccessListener {
                service.changeIsReadGroup(groupChat?.id ?: "", sharedPref.userId, false)
            }
    }

    private fun <T> remove(list: MutableList<T>, predicate: Predicate<T>) {
        list.removeIf { x: T -> predicate.test(x) }
    }
}