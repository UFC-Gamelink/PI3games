package com.gamelink.gamelinkapp.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.gamelink.gamelinkapp.databinding.FragmentCommunitiesBinding
import com.gamelink.gamelinkapp.service.listener.CommunityListener
import com.gamelink.gamelinkapp.view.adapter.CommunityAdapter
import com.gamelink.gamelinkapp.view.adapter.ViewPagerCommunitiesAdapter
import com.gamelink.gamelinkapp.viewmodel.CommunitiesViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener

class CommunitiesFragment : Fragment() {
    private lateinit var viewModel: CommunitiesViewModel
    private var _binding: FragmentCommunitiesBinding? = null

    private val binding get() = _binding!!
    private val communityAdapter = CommunityAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(CommunitiesViewModel::class.java)
        _binding = FragmentCommunitiesBinding.inflate(inflater, container, false)

        binding.recyclerCommunities.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerCommunities.adapter = communityAdapter

        val listener = object : CommunityListener {
            override fun onCommunityClick(id: String) {
                val intent = Intent(context, CommunityActivity::class.java)
                val bundle = Bundle()
                bundle.putString("community_id", id)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }

        communityAdapter.attachListener(listener)

        setViewPager()
        setListeners()

        observe()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.list()
    }

    private fun observe() {
        viewModel.communities.observe(viewLifecycleOwner) {
            communityAdapter.updateCommunities(it)
        }
    }

    private fun setListeners() {
        binding.floatingActionButton.setOnClickListener {
            startActivity(Intent(context, CommunityFormActivity::class.java))
        }
    }

    private fun setViewPager() {
        val tabLayout = binding.tabLayoutCommunities
        val viewPager = binding.viewPagerCommunities

        val viewpagerCommunitiesAdapter = ViewPagerCommunitiesAdapter(this)

        viewPager.adapter = viewpagerCommunitiesAdapter

        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // TODO("Not yet implemented")
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // TODO("Not yet implemented")
            }
        })

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.getTabAt(position)?.select()
            }
        })
    }

}