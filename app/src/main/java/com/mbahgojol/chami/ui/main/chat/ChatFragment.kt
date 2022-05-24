package com.mbahgojol.chami.ui.main.chat

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat.getSystemService
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mbahgojol.chami.R
import com.mbahgojol.chami.databinding.FragmentChatBinding


class ChatFragment : Fragment() {
    private lateinit var viewPager: ViewPager2
    private lateinit var binding: FragmentChatBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?){
        super.onActivityCreated(savedInstanceState)

        val fragmentView = requireNotNull(view) {"View should not be null when calling onActivityCreated"}

        val sectionsPagerAdapter = SectionsPagerAdapter(childFragmentManager, lifecycle)
        viewPager = fragmentView.findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = fragmentView.findViewById(R.id.tbHome)

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_chat_1,
            R.string.tab_chat_2
        )
    }

}