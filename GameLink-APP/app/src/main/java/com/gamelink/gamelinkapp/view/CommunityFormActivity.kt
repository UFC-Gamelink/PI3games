package com.gamelink.gamelinkapp.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.gamelink.gamelinkapp.databinding.ActivityCommunityFormBinding
import com.gamelink.gamelinkapp.service.model.CommunityModel
import com.gamelink.gamelinkapp.utils.ImageUtils
import com.gamelink.gamelinkapp.viewmodel.CommunityFormViewModel

class CommunityFormActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCommunityFormBinding
    private lateinit var viewModel: CommunityFormViewModel
    private var imageUri: Uri? = null
    private var communityId = ""

    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            result.data?.data.let { uri ->
                if (uri != null) {
                    imageUri = uri
                    binding.imageBannerCommunity.scaleType = ImageView.ScaleType.CENTER_CROP
                    Glide.with(this).load(uri).into(binding.imageBannerCommunity)

                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(CommunityFormViewModel::class.java)
        binding = ActivityCommunityFormBinding.inflate(layoutInflater)

        binding.imageBack.setOnClickListener {
            finish()
        }

        binding.buttonCreateCommunity.setOnClickListener {
            handleSave()
        }

        binding.imageBannerCommunity.setOnClickListener {
            chooseImage()
        }

        loadDataFromActivity()

        observe()

        setContentView(binding.root)
    }

    private fun loadDataFromActivity() {
        val bundle = intent.extras

        if (bundle != null) {
            communityId = bundle.getString("community_id").toString()

            viewModel.load(communityId)
        }
    }

    private fun handleSave() {
        val communityBannerPath = ImageUtils.saveImageUri(applicationContext, imageUri)

        val community = CommunityModel().apply {
            this.id = communityId
            this.name = binding.edittextCommunityName.text.toString()
            this.description = binding.edittextCommunityDescription.text.toString()
            //this.private = binding.switchPrivateCommunity.isChecked
            //this.bannerUrl = communityBannerPath
        }

        viewModel.save(community)
    }

    private fun chooseImage() {
        getContent.launch(
            Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
        )
    }

    private fun observe() {
        viewModel.communitySave.observe(this) {
            if (it.status()) {
                if (communityId == "") {
                    toast("salvo com sucesso")
                } else {
                    toast("atualizado com sucesso")
                }

                finish()
            } else {
                Toast.makeText(applicationContext, it.message(), Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.community.observe(this) {
            binding.edittextCommunityName.setText(it.name)
            binding.edittextCommunityDescription.setText(it.description)

            if(it.bannerUrl != null) {
                Glide.with(this).load(it.bannerUrl).into(binding.imageBannerCommunity)
                binding.imageBannerCommunity.scaleType = ImageView.ScaleType.CENTER_CROP
                imageUri = Uri.parse(it.bannerUrl)
            }
            //binding.switchPrivateCommunity.isChecked = it.private
        }
    }

    private fun toast(str: String) {
        Toast.makeText(applicationContext, str, Toast.LENGTH_SHORT).show()
    }
}