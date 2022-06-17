package com.mbahgojol.chami.ui.main.chat.group

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.toObjects
import com.mbahgojol.chami.data.SharedPref
import com.mbahgojol.chami.data.model.GrupChat
import com.mbahgojol.chami.data.remote.FirestoreService
import com.mbahgojol.chami.databinding.FragmentListGroupBinding
import com.mbahgojol.chami.ui.main.chat.group.converse.GroupChatActivity
import com.mbahgojol.chami.ui.main.chat.group.create.CreateGroupActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ListGroupFragment : Fragment() {
    private var binding: FragmentListGroupBinding? = null

    @Inject
    lateinit var service: FirestoreService

    @Inject
    lateinit var sharedPref: SharedPref

    private val myAdapter by lazy {
        ListGroupAdapter(sharedPref.userId, service) {
            val i = Intent(requireActivity(), GroupChatActivity::class.java)
            i.putExtra("id", it.id)
            startActivity(i)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListGroupBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            addGroup.setOnClickListener {
                startActivity(Intent(requireActivity(), CreateGroupActivity::class.java))
            }

            rvListChat.layoutManager = LinearLayoutManager(requireActivity())
            rvListChat.setHasFixedSize(true)
            rvListChat.adapter = myAdapter
        }

        service.getGroup(sharedPref.userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Timber.d("Listen failed.")
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val model = snapshot.toObjects<GrupChat>().toMutableList()
                    myAdapter.setData(model)
                }
            }
    }
}