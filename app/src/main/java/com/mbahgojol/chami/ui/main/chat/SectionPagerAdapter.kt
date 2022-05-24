package com.mbahgojol.chami.ui.main.chat

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mbahgojol.chami.ui.main.chat.group.GroupChatFragment
import com.mbahgojol.chami.ui.main.chat.personal.PersonalChatFragment

class SectionsPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = PersonalChatFragment()
            1 -> fragment = GroupChatFragment()
        }
        return fragment as Fragment
    }

}