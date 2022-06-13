package com.mbahgojol.chami.ui.main.challanges

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mbahgojol.chami.R
import com.mbahgojol.chami.ui.main.chat.SectionsPagerAdapter


class ChallangesFragment : Fragment() {
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_challanges, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?){
        super.onActivityCreated(savedInstanceState)

        val fragmentView = requireNotNull(view) {"View should not be null when calling onActivityCreated"}

        val sectionsPagerAdapter = ChallengePagerAdapter(childFragmentManager, lifecycle)
        viewPager = fragmentView.findViewById(R.id.viewPager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = fragmentView.findViewById(R.id.tbChallenge)

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_challenge_1,
            R.string.tab_challenge_2
        )
    }
}