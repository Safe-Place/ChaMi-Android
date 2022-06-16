package com.mbahgojol.chami.ui.main.chat.group.create

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.toObjects
import com.mbahgojol.chami.data.SharedPref
import com.mbahgojol.chami.data.model.GrupChat
import com.mbahgojol.chami.data.model.Users
import com.mbahgojol.chami.data.remote.FirestoreService
import com.mbahgojol.chami.databinding.ActivityCreateGroupBinding
import com.mbahgojol.chami.ui.main.chat.personal.converse.setStatusBarGradiant
import com.mbahgojol.chami.utils.DateUtils
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*
import java.util.function.Predicate
import javax.inject.Inject

@AndroidEntryPoint
class CreateGroupActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateGroupBinding

    @Inject
    lateinit var service: FirestoreService

    @Inject
    lateinit var sharedPref: SharedPref

    private val myAdapter by lazy {
        UserAdapter {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarGradiant(this)
        binding = ActivityCreateGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.apply {
            rvList.apply {
                layoutManager = LinearLayoutManager(this@CreateGroupActivity)
                setHasFixedSize(true)
                adapter = myAdapter
            }

            btnCreate.setOnClickListener {
                val listUser: MutableList<String> = myAdapter.selectedData.map {
                    it.user_id
                } as MutableList<String>
                listUser.add(sharedPref.userId)

                val group = GrupChat(
                    sharedPref.userId,
                    etNamaGroup.text.toString(),
                    "https://cdn5.vectorstock.com/i/thumb-large/84/89/group-chat-contact-icon-office-team-or-teamwork-vector-23828489.jpg",
                    "",
                    "New Group",
                    "",
                    "",
                    DateUtils.getCurrentTime(),
                    UUID.randomUUID().toString(),
                    listUser
                )

                service.addGroup(group)
                    .onSuccessTask {
                        service.createRoom(group.id)
                    }
                    .addOnCompleteListener {
                        finish()
                    }
            }
        }

        service.getAllUser()
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null && !snapshot.isEmpty) {
                    val model = snapshot.toObjects<Users>().toMutableList()
                    val prediction = Predicate<Users> { it.user_id == sharedPref.userId }
                    remove(model, prediction)
                    myAdapter.setData(model)
                } else {
                    Timber.e("Tidak ada List Chat")
                }
            }
    }

    private fun <T> remove(list: MutableList<T>, predicate: Predicate<T>) {
        list.removeIf { x: T -> predicate.test(x) }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}