package com.gamelink.gamelinkapp.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
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
    private lateinit var dialog: AlertDialog
    private var bannerPreview: Bitmap? = null
    private var bannerPath: String? = null
    private lateinit var community: CommunityModel

    private val requestGallery =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { permission ->
            if (permission) {
                resultGallery.launch(
                    Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                )
            } else {
                showDialogPermission()
            }
        }

    private val resultGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.data?.data != null) {
                val bitmap: Bitmap = if (Build.VERSION.SDK_INT < 28) {
                    MediaStore.Images.Media.getBitmap(
                        baseContext.contentResolver,
                        result.data?.data
                    )
                } else {
                    val source = ImageDecoder.createSource(
                        this.contentResolver,
                        result.data?.data!!
                    )
                    ImageDecoder.decodeBitmap(source)
                }
                bannerPreview = bitmap

                binding.imageBannerCommunity.setImageBitmap(bitmap)
            }
        }

    companion object {
        private val PERMISSION_GALLERY = if (Build.VERSION.SDK_INT < 33) {
            Manifest.permission.READ_EXTERNAL_STORAGE
        } else {
            Manifest.permission.READ_MEDIA_IMAGES
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
            checkGalleryPermission()
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
        bannerPreview?.let { saveImage(it) }

        community = CommunityModel().apply {
            this.id = communityId
            this.name = binding.edittextCommunityName.text.toString()
            this.description = binding.edittextCommunityDescription.text.toString()
            this.bannerUrl = bannerPath
        }

        viewModel.save(community)
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

            if (it.bannerUrl != null) {
                Glide.with(this).load(it.bannerUrl).into(binding.imageBannerCommunity)
                binding.imageBannerCommunity.scaleType = ImageView.ScaleType.CENTER_CROP
                imageUri = Uri.parse(it.bannerUrl)
            }
            //binding.switchPrivateCommunity.isChecked = it.private
        }

        viewModel.idCommunity.observe(this) {
            community.id = it
            viewModel.saveBanner(community)
        }
    }

    private fun toast(str: String) {
        Toast.makeText(applicationContext, str, Toast.LENGTH_SHORT).show()
    }

    private fun checkGalleryPermission() {
        val galleryPermissionAccepted =
            checkPermission(PERMISSION_GALLERY)

        when {
            galleryPermissionAccepted -> {
                resultGallery.launch(
                    Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                )
            }
            // Usuario nao aceitou a permissao
            shouldShowRequestPermissionRationale(PERMISSION_GALLERY) -> showDialogPermission()
            else -> requestGallery.launch(PERMISSION_GALLERY)
        }
    }

    private fun showDialogPermission() {
        val builder = AlertDialog.Builder(this)
            .setTitle("Atenção")
            .setMessage("Precisamos do acesso a galeria do dispositivo, deseja permitir agora?")
            .setNegativeButton("Não") { _, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Sim") { _, _ ->
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", packageName, null)
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                dialog.dismiss()
            }

        dialog = builder.create()
        dialog.show()
    }

    private fun checkPermission(permission: String) =
        ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

    private fun saveImage(bitmap: Bitmap) {
        val absolutePath = ImageUtils.saveImage(applicationContext, "banner-image", bitmap)
        bannerPath = absolutePath
    }
}