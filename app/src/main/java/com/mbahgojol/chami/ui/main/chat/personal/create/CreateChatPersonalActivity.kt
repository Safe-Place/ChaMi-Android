package com.mbahgojol.chami.ui.main.chat.personal.create

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.mbahgojol.chami.R
import com.mbahgojol.chami.data.SharedPref
import com.mbahgojol.chami.data.model.ChatRoom
import com.mbahgojol.chami.data.model.Detail
import com.mbahgojol.chami.data.model.Users
import com.mbahgojol.chami.data.remote.FirestoreService
import com.mbahgojol.chami.databinding.ActivityCreateChatPersonalBinding
import com.mbahgojol.chami.ui.main.chat.personal.converse.PersonalChatActivity
import com.mbahgojol.chami.ui.main.search.SearchUserAdapter
import com.mbahgojol.chami.utils.BaseActivity
import com.mbahgojol.chami.utils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.function.Predicate
import javax.inject.Inject

@AndroidEntryPoint
class CreateChatPersonalActivity : BaseActivity() {

    private lateinit var binding: ActivityCreateChatPersonalBinding

    @Inject
    lateinit var service: FirestoreService

    @Inject
    lateinit var sharedPref: SharedPref

    private val myAdapter by lazy {
        SearchUserAdapter {
            service.getListChat(sharedPref.userId)
                .whereEqualTo("receiver_id", it.user_id)
                .get()
                .addOnSuccessListener { snap ->
                    if (snap.documents.isNotEmpty()) {
                        val room = snap.documents[0].toObject<ChatRoom>()
                        service.getChatDetail(sharedPref.userId, room?.roomid ?: "")
                            .get()
                            .addOnSuccessListener { snapshot ->
                                if (snapshot != null && snapshot.exists()) {
                                    val detail = snapshot.toObject<Detail>()
                                    Intent(
                                        this@CreateChatPersonalActivity,
                                        PersonalChatActivity::class.java
                                    ).apply {
                                        putExtra("data", room)
                                        putExtra("isread", detail?.isread)
                                        startActivity(this)
                                        finish()
                                    }
                                }
                            }
                    } else {
                        Intent(
                            this@CreateChatPersonalActivity,
                            PersonalChatActivity::class.java
                        ).apply {
                            putExtra("users", it)
                            startActivity(this)
                            finish()
                        }
                    }
                }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateChatPersonalBinding.inflate(layoutInflater)
        binding.root.hideKeyboard()
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.elevation = 0f
        supportActionBar?.title = "Pilih contact"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.apply {
            rvSearch.apply {
                layoutManager = LinearLayoutManager(this@CreateChatPersonalActivity)
                adapter = myAdapter
                setHasFixedSize(true)
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_view, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val menuItem = menu.findItem(R.id.search)
        val searchView = menuItem.actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint_user)

        menuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                return true
            }

        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Toast.makeText(this@CreateChatPersonalActivity, query, Toast.LENGTH_SHORT).show()
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun <T> remove(list: MutableList<T>, predicate: Predicate<T>) {
        list.removeIf { x: T -> predicate.test(x) }
    }
}