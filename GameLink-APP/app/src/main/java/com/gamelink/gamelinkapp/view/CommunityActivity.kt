package com.gamelink.gamelinkapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.gamelink.gamelinkapp.R
import com.gamelink.gamelinkapp.databinding.ActivityCommunityBinding
import com.gamelink.gamelinkapp.viewmodel.CommunityViewModel

class CommunityActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCommunityBinding
    private lateinit var viewModel: CommunityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCommunityBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(CommunityViewModel::class.java)

        loadDataFromActivity()

        observe()

        setContentView(binding.root)
    }

    private fun loadDataFromActivity() {
        val bundle = intent.extras

        if(bundle != null) {
            val communityId = bundle.getInt("community_id")

            viewModel.load(communityId)
            viewModel.isOwner()
        }
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
            }
        }
    }
}