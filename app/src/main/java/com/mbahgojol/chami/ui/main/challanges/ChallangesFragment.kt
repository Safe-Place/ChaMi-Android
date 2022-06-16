package com.mbahgojol.chami.ui.main.challanges

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mbahgojol.chami.LoginPref
import com.mbahgojol.chami.R
import com.mbahgojol.chami.ui.main.chat.SectionsPagerAdapter
import com.mbahgojol.chami.ui.main.others.ChamiPictActivity


class ChallangesFragment : Fragment() {
    private lateinit var viewPager: ViewPager2
    private lateinit var btnAdd : FloatingActionButton

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

        btnAdd = fragmentView.findViewById(R.id.fb_add_challenge)

        val jabatan = LoginPref(requireContext()).getPosisi()
        if (jabatan == "Agent"){
            btnAdd.isVisible = false
        }

        btnAdd.setOnClickListener {
            val intent = Intent(requireContext(), TambahChallengeActivity::class.java)
            startActivity(intent)
        }


    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_challenge_1,
            R.string.tab_challenge_2
        )
    }
}