package com.gamelink.gamelinkapp.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.gamelink.gamelinkapp.view.fragments.CollectionsUserFragment
import com.gamelink.gamelinkapp.view.fragments.CommunitiesUserFragment
import com.gamelink.gamelinkapp.view.fragments.PostsUserFragment

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> PostsUserFragment()
            1 -> CommunitiesUserFragment()
            2 -> CollectionsUserFragment()
            else -> PostsUserFragment()
        }
    }
}