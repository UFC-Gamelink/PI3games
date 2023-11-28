package com.gamelink.gamelinkapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gamelink.gamelinkapp.databinding.FragmentHomeBinding
import com.gamelink.gamelinkapp.service.listener.PostListener
import com.gamelink.gamelinkapp.view.adapter.PostAdapter
import com.gamelink.gamelinkapp.viewmodel.HomeViewModel

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel
    private val adapter: PostAdapter = PostAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.recyclerPostsHome.layoutManager = LinearLayoutManager(context)


        viewModel.getRecommendedPosts()
        observe()

        adapter.attachListener(object : PostListener {
            override fun onDeleteClick(id: String) {
                viewModel.delete(id)
            }

            override fun onLikeClick(id: String) {
                TODO("Not yet implemented")
            }
        })

        binding.recyclerPostsHome.adapter = adapter
        return binding.root
    }

    private fun observe() {
        viewModel.posts.observe(viewLifecycleOwner) {
            adapter.updatePosts(it)
        }
    }
}