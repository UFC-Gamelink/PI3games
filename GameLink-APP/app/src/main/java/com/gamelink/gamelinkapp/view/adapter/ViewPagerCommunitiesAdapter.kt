package com.gamelink.gamelinkapp.view.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.gamelink.gamelinkapp.view.CommunitiesPostsFragment
import com.gamelink.gamelinkapp.view.CommunitiesTopicsFragment

class ViewPagerCommunitiesAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> CommunitiesPostsFragment()
            1 -> CommunitiesTopicsFragment()
            else -> CommunitiesPostsFragment()
        }
    }
}