package com.gamelink.gamelinkapp.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gamelink.gamelinkapp.databinding.FragmentPostsUserBinding
import com.gamelink.gamelinkapp.view.adapter.PostAdapter
import com.gamelink.gamelinkapp.viewmodel.PostsUserViewModel

class PostsUserFragment : Fragment() {
    private lateinit var viewModel: PostsUserViewModel
    private var _binding: FragmentPostsUserBinding? = null
    private val binding get() = _binding!!
    private val adapter = PostAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(PostsUserViewModel::class.java)
        _binding = FragmentPostsUserBinding.inflate(inflater, container, false)

        binding.recyclerPostsUser.layoutManager = LinearLayoutManager(context)
        binding.recyclerPostsUser.adapter = adapter

        observe()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.list()
    }

    private fun observe() {
        viewModel.posts.observe(viewLifecycleOwner) {
            adapter.updateTasks(it)
        }
    }
}