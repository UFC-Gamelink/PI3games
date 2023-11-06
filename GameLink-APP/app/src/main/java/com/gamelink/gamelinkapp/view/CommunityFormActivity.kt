package com.gamelink.gamelinkapp.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.gamelink.gamelinkapp.R
import com.gamelink.gamelinkapp.databinding.ActivityCommunityFormBinding
import com.gamelink.gamelinkapp.service.model.CommunityModel
import com.gamelink.gamelinkapp.utils.ImageUtils
import com.gamelink.gamelinkapp.viewmodel.CommunityFormViewModel

class CommunityFormActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCommunityFormBinding
    private lateinit var viewModel: CommunityFormViewModel

    private var imageUri: Uri? = null

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if(uri != null) {
                imageUri = uri
                binding.imageBannerCommunity.setImageURI(imageUri)
            } else {
                binding.imageBannerCommunity.setImageDrawable(getDrawable(R.drawable.ic_new_photo))
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

        setContentView(binding.root)

        observe()
    }

    private fun handleSave() {
        val communityBannerPath = ImageUtils.saveImageUri(applicationContext, imageUri)

        val community = CommunityModel().apply {
            this.name = binding.edittextCommunityName.text.toString()
            this.description = binding.edittextCommunityDescription.text.toString()
            this.private = binding.switchPrivateCommunity.isChecked
            this.bannerUrl = communityBannerPath
        }

        viewModel.save(community)
    }

    private fun chooseImage() {
        getContent.launch("image/*")
    }

    private fun observe() {
        viewModel.communitySave.observe(this) {
            if(it.status()) {
                Toast.makeText(applicationContext, "salvo com sucesso", Toast.LENGTH_SHORT).show()
                imageUri = null
                finish()
            } else {
                Toast.makeText(applicationContext, it.message(),Toast.LENGTH_SHORT).show()
            }
        }
    }
}