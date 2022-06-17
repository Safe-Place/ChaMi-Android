package com.mbahgojol.chami.ui.main.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.mbahgojol.chami.R
import com.mbahgojol.chami.data.SharedPref
import com.mbahgojol.chami.data.remote.FirestoreService
import com.mbahgojol.chami.databinding.FragmentChatBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding

    @Inject
    lateinit var service: FirestoreService

    @Inject
    lateinit var sharedPref: SharedPref

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sectionsPagerAdapter = SectionsPagerAdapter(childFragmentManager, lifecycle)
        binding.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tbHome, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()


        service.getAllCountNotif(sharedPref.userId)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Timber.d("Listen failed.")
                    return@addSnapshotListener
                }

                if (value != null) {
                    val size = value.documents.size
                    if (size == 0) {
                        binding.tbHome.getTabAt(0)?.removeBadge()

                        findNavController().getBackStackEntry(R.id.chatFragment)
                            .savedStateHandle["haveCount"] = false
                    } else {
                        binding.tbHome.getTabAt(0)?.orCreateBadge?.number = size
                        binding.tbHome.getTabAt(0)?.badge?.backgroundColor =
                            ContextCompat.getColor(view.context, R.color.white)
                        binding.tbHome.getTabAt(0)?.badge?.badgeTextColor =
                            ContextCompat.getColor(view.context, R.color.oren)

                        findNavController().getBackStackEntry(R.id.chatFragment)
                            .savedStateHandle["haveCount"] = true
                    }
                }
            }

        service.getAllNotifCountGroup(sharedPref.userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Timber.d("Listen failed.")
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val size = snapshot.documents.size
                    if (size <= 0) {
                        binding.tbHome.getTabAt(1)?.removeBadge()

                        findNavController().getBackStackEntry(R.id.chatFragment)
                            .savedStateHandle["haveCountGroup"] = false
                    } else {
                        binding.tbHome.getTabAt(1)?.orCreateBadge?.number = size
                        binding.tbHome.getTabAt(1)?.badge?.backgroundColor =
                            ContextCompat.getColor(view.context, R.color.white)
                        binding.tbHome.getTabAt(1)?.badge?.badgeTextColor =
                            ContextCompat.getColor(view.context, R.color.oren)

                        findNavController().getBackStackEntry(R.id.chatFragment)
                            .savedStateHandle["haveCountGroup"] = true
                    }
                }
            }
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_chat_1,
            R.string.tab_chat_2
        )
    }

}