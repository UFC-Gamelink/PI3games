package com.gamelink.gamelinkapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gamelink.gamelinkapp.databinding.FragmentCommunitiesPostsBinding
import com.gamelink.gamelinkapp.view.adapter.PostAdapter
import com.gamelink.gamelinkapp.viewmodel.CommunitiesPostsViewModel

class CommunitiesPostsFragment : Fragment() {
    private lateinit var viewModel: CommunitiesPostsViewModel
    private var _binding: FragmentCommunitiesPostsBinding? = null
    private val binding get() = _binding!!
    private val adapter = PostAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommunitiesPostsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(CommunitiesPostsViewModel::class.java)

        binding.recyclerPostsCommunity.layoutManager = LinearLayoutManager(context)
        binding.recyclerPostsCommunity.adapter = adapter

        //viewModel.list()

        observe()

        return binding.root
    }

    private fun observe() {
        viewModel.posts.observe(viewLifecycleOwner) {
            adapter.updatePosts(it)
        }
    }
}