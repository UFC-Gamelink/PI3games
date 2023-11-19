package com.gamelink.gamelinkapp.view.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.gamelink.gamelinkapp.view.CommunityPostsFragment

class ViewPagerCommunityAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {
    private var communityId: Int = 0

    override fun getItemCount(): Int = 1

    override fun createFragment(position: Int): Fragment {
        val fragment = when(position) {
            0 -> CommunityPostsFragment()
            else -> CommunityPostsFragment()
        }

        val bundle = Bundle()
        bundle.putInt("community_id", communityId)
        fragment.arguments = bundle

        return fragment
    }

    fun setCommunityId(id: Int) {
        communityId = id
    }
}