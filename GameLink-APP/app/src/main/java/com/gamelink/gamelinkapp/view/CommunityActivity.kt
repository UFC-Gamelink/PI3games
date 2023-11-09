package com.gamelink.gamelinkapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.gamelink.gamelinkapp.databinding.ActivityCommunityBinding
import com.gamelink.gamelinkapp.viewmodel.CommunityViewModel

class CommunityActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCommunityBinding
    private lateinit var viewModel: CommunityViewModel
    private lateinit var bundle: Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCommunityBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(CommunityViewModel::class.java)

        bundle = intent.extras!!

        loadDataFromActivity()

        binding.imageBack.setOnClickListener {
            finish()
        }

        binding.buttonJoinCommunity.setOnClickListener {
            val communityId = bundle.getInt("community_id")

            viewModel.join(communityId)
        }

        binding.buttonLeaveCommunity.setOnClickListener {
            val communityId = bundle.getInt("community_id")

            viewModel.leave(communityId)
        }

        observe()

        setContentView(binding.root)
    }

    private fun loadDataFromActivity() {
        val communityId = bundle.getInt("community_id")

        viewModel.load(communityId)
        viewModel.isOwner()
    }

    private fun observe() {
        viewModel.community.observe(this) {
            binding.textName.text = it.name
            binding.textDescription.text = it.description
            Glide.with(this).load(it.bannerUrl).into(binding.imageBanner)
        }

        viewModel.userIsOwner.observe(this) {
            if(it) {
                binding.buttonJoinCommunity.visibility = View.GONE
                binding.buttonLeaveCommunity.visibility = View.GONE
            } else {
                val communityId = bundle.getInt("community_id")

                viewModel.joined(communityId)
            }
        }

        viewModel.joined.observe(this) {
            if(it) {
                binding.buttonJoinCommunity.visibility = View.GONE
                binding.buttonLeaveCommunity.visibility = View.VISIBLE
            } else {
                binding.buttonLeaveCommunity.visibility = View.GONE
                binding.buttonJoinCommunity.visibility = View.VISIBLE
            }
        }
    }
}