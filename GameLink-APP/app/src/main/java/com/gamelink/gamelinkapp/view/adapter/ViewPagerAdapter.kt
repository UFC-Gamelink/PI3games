package com.gamelink.gamelinkapp.view.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.gamelink.gamelinkapp.view.fragments.PostsUserFragment

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 1

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> PostsUserFragment()
            else -> PostsUserFragment()
        }
    }
}