package com.mbahgojol.chami.ui.main.chat.personal

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.toObject
import com.mbahgojol.chami.data.ListChatModel
import com.mbahgojol.chami.databinding.FragmentPersonalChatBinding
import com.mbahgojol.chami.di.FirestoreService
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class PersonalChatFragment : Fragment() {
    private var binding: FragmentPersonalChatBinding? = null

    @Inject
    lateinit var firestoreModule: FirestoreService
    private val listAdapter by lazy {
        PersonalChatAdapter {
            Intent(requireActivity(), DetailPersonalChatActivity::class.java).apply {
                putExtra("data", it)
                startActivity(this)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPersonalChatBinding.inflate(inflater, container, false)
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

        firestoreModule.listenListChatById("msqzZHdcpHi6og1uBUmo")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Timber.d("Listen failed.")
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val model = snapshot.toObject<ListChatModel>()
                    model?.let { listAdapter.setData(it.history_chat) }
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