package com.mbahgojol.chami.ui.main.chat.personal

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.toObjects
import com.mbahgojol.chami.data.SharedPref
import com.mbahgojol.chami.data.model.ChatRoom
import com.mbahgojol.chami.data.remote.FirestoreService
import com.mbahgojol.chami.databinding.FragmentPersonalListBinding
import com.mbahgojol.chami.ui.main.chat.personal.converse.PersonalChatActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ListPersonalFragment : Fragment() {
    private var binding: FragmentPersonalListBinding? = null

    @Inject
    lateinit var firestoreModule: FirestoreService

    @Inject
    lateinit var sharedPref: SharedPref

    private val viewModel: ListPersonalViewModel by viewModels()

    private val listAdapter by lazy {
        ListPersonalAdapter(sharedPref.userId, firestoreModule) { room, isread ->
            Intent(requireActivity(), PersonalChatActivity::class.java).apply {
                putExtra("data", room)
                putExtra("isInit", false)
                startActivity(this)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPersonalListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            rvListChat.apply {
                layoutManager = LinearLayoutManager(requireActivity())
                adapter = listAdapter
                setHasFixedSize(true)
            }
        }

        viewModel.listenList.observe(viewLifecycleOwner) {
        }

        firestoreModule.getListChatHome(sharedPref.userId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Timber.d("Listen failed.")
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val model = snapshot.toObjects<ChatRoom>().toMutableList()
                    listAdapter.setData(model)
                } else {
                    Timber.e("Tidak ada List Chat")
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}