package com.gamelink.gamelinkapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gamelink.gamelinkapp.databinding.FragmentCommunityPostsBinding
import com.gamelink.gamelinkapp.service.listener.PostListener
import com.gamelink.gamelinkapp.view.adapter.PostAdapter
import com.gamelink.gamelinkapp.viewmodel.CommunityPostsViewModel


class CommunityPostsFragment : Fragment() {
    private var _binding: FragmentCommunityPostsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CommunityPostsViewModel
    private val adapter = PostAdapter()
    private var communityId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommunityPostsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(CommunityPostsViewModel::class.java)

        binding.recyclerCommunityPosts.layoutManager = LinearLayoutManager(context)
        binding.recyclerCommunityPosts.adapter = adapter

        arguments?.let {
            communityId = it.getInt("community_id")
        }

        val listener = object : PostListener {
            override fun onDeleteClick(id: String) {
                viewModel.delete(id)
            }

            override fun onLikeClick(id: String) {
                TODO("Not yet implemented")
            }
        }

        adapter.attachListener(listener)

        observe()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.list(communityId)
    }

    private fun observe() {
        viewModel.posts.observe(viewLifecycleOwner) {
            adapter.updatePosts(it)
        }

        viewModel.delete.observe(viewLifecycleOwner) {
            viewModel.list(communityId)
        }
    }
}