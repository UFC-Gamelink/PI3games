package com.gamelink.gamelinkapp.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gamelink.gamelinkapp.databinding.FragmentCommunitiesBinding
import com.gamelink.gamelinkapp.service.listener.CommunityListener
import com.gamelink.gamelinkapp.view.adapter.CommunityAdapter
import com.gamelink.gamelinkapp.viewmodel.CommunitiesViewModel

class CommunitiesFragment : Fragment() {
    private lateinit var viewModel: CommunitiesViewModel
    private var _binding: FragmentCommunitiesBinding? = null

    private val binding get() = _binding!!
    private val adapter = CommunityAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(CommunitiesViewModel::class.java)
        _binding = FragmentCommunitiesBinding.inflate(inflater, container, false)

        binding.recyclerCommunities.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerCommunities.adapter = adapter

        val listener = object : CommunityListener {
            override fun onCommunityClick(id: Int) {
                val intent = Intent(context, CommunityActivity::class.java)
                val bundle = Bundle()
                bundle.putInt("community_id", id)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }

        adapter.attachListener(listener)

        binding.floatingActionButton.setOnClickListener {
            startActivity(Intent(context, CommunityFormActivity::class.java))
        }

        viewModel.list()

        observe()

        return binding.root
    }

    private fun observe() {
        viewModel.communities.observe(viewLifecycleOwner) {
            adapter.updateCommunities(it)
        }
    }

}