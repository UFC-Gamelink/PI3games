package com.gamelink.gamelinkapp.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gamelink.gamelinkapp.databinding.FragmentCommunitiesBinding

class CommunitiesFragment : Fragment() {
    private var _binding: FragmentCommunitiesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommunitiesBinding.inflate(inflater, container, false)


        binding.floatingActionButton.setOnClickListener {
            startActivity(Intent(context, CommunityFormActivity::class.java))
        }
        return binding.root
    }

}