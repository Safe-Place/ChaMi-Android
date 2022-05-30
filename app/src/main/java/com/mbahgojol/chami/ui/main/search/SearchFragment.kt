package com.mbahgojol.chami.ui.main.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.mbahgojol.chami.data.SharedPref
import com.mbahgojol.chami.data.model.ChatRoom
import com.mbahgojol.chami.data.model.Users
import com.mbahgojol.chami.databinding.FragmentSearchBinding
import com.mbahgojol.chami.di.FirestoreService
import com.mbahgojol.chami.ui.main.chat.personal.detail.DetailPersonalChatActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.function.Predicate
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment() {

    @Inject
    lateinit var service: FirestoreService

    @Inject
    lateinit var sharedPref: SharedPref

    private var binding: FragmentSearchBinding? = null
    private val myAdapter by lazy {
        SearchUserAdapter {
            service.getListChat(sharedPref.userId)
                .whereEqualTo("receiver_id", it.user_id)
                .get()
                .addOnSuccessListener { snap ->
                    if (snap.documents.isNotEmpty()) {
                        val room = snap.documents[0].toObject<ChatRoom>()
                        Intent(requireActivity(), DetailPersonalChatActivity::class.java).apply {
                            putExtra("data", room)
                            putExtra("senderId", sharedPref.userId)
                            startActivity(this)
                        }
                    } else {
                        Intent(requireActivity(), DetailPersonalChatActivity::class.java).apply {
                            putExtra("users", it)
                            putExtra("senderId", sharedPref.userId)
                            startActivity(this)
                        }
                    }
                }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            rvSearch.apply {
                layoutManager = LinearLayoutManager(requireActivity())
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

    private fun <T> remove(list: MutableList<T>, predicate: Predicate<T>) {
        list.removeIf { x: T -> predicate.test(x) }
    }
}