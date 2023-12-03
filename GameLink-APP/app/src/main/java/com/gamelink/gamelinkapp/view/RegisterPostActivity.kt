package com.gamelink.gamelinkapp.view

import android.Manifest
import android.app.Activity
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
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.gamelink.gamelinkapp.R
import com.gamelink.gamelinkapp.databinding.ActivityRegisterPostBinding
import com.gamelink.gamelinkapp.service.model.CommunityModel
import com.gamelink.gamelinkapp.service.model.PostModel
import com.gamelink.gamelinkapp.utils.ImageUtils
import com.gamelink.gamelinkapp.viewmodel.RegisterPostViewModel

class RegisterPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterPostBinding
    private lateinit var viewModel: RegisterPostViewModel
    private var listCommunities: List<CommunityModel> = mutableListOf()
    private var imagePreview: Bitmap? = null
    private lateinit var dialog: AlertDialog
    private var imagePath: String? = null



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

    private val activityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                data?.let {
                    val bundle = it.extras!!

                    val uriString = bundle.getString("photo_uri")

                    if(uriString != null) {
                        val uri = Uri.parse(uriString)

                        val bitmap: Bitmap = if (Build.VERSION.SDK_INT < 28) {
                            MediaStore.Images.Media.getBitmap(baseContext.contentResolver, uri)
                        } else {
                            val source = ImageDecoder.createSource(this.contentResolver, uri)
                            ImageDecoder.decodeBitmap(source)
                        }

                        imagePreview = bitmap

                        binding.imagePreview.setImageBitmap(bitmap)

                        binding.imageRemoveImage.visibility = View.VISIBLE
                    }
                }
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
                imagePreview = bitmap

                binding.imagePreview.setImageBitmap(bitmap)

                binding.imageRemoveImage.visibility = View.VISIBLE
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


        viewModel = ViewModelProvider(this).get(RegisterPostViewModel::class.java)

        binding = ActivityRegisterPostBinding.inflate(layoutInflater)

        binding.buttonClose.setOnClickListener {
            finish()
        }

        binding.imageAddImage.setOnClickListener {
            showPopupMenu(it)
        }

        observe()

        binding.buttonPost.setOnClickListener { handlePost() }

        binding.imageRemoveImage.setOnClickListener {
            imagePath = null
            imagePreview = null
            binding.imagePreview.setImageBitmap(null)
            binding.imageRemoveImage.visibility = View.GONE
        }

        viewModel.loadCommunities()

        setContentView(binding.root)
    }

    private fun handlePost() {
        imagePreview?.let { saveImage(it) }

        val post = PostModel().apply {
            this.description = binding.editPost.text.toString().trim()
            this.imageUrl = imagePath
        }

        val index = binding.spinnerVisibility.selectedItemPosition
        if(index == 0) {
            viewModel.save(post)

        } else {
            val communityId = listCommunities[index - 1].id
            viewModel.saveForCommunity(post, communityId)
        }
    }

    private fun observe() {
        viewModel.postSave.observe(this) {
            if (it.status()) {
                Toast.makeText(applicationContext, "Salvo com sucesso", Toast.LENGTH_SHORT).show()
                imagePreview = null
                imagePath = null
                finish()
            } else {
                Toast.makeText(applicationContext, it.message(), Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.communityList.observe(this) {
            listCommunities = it

            val list = mutableListOf<String>()

            list.add("Público")
            for (c in it) {
                list.add(c.name)
            }

            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list)
            binding.spinnerVisibility.adapter = adapter
        }

    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(applicationContext, view).apply {
            menuInflater.inflate(R.menu.options_images, this.menu)
        }

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.item_camera_select -> {
                    val intent =  Intent(this, CameraActivity::class.java)
                    activityResultLauncher.launch(intent)
                    true
                }
                R.id.item_gallery_select -> {
                    checkGalleryPermission()
                    true
                }
                else -> false
            }
        }

        popupMenu.show()
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

    private fun saveImage(bitmap: Bitmap, quality: Int = 60) {
        val absolutePath = ImageUtils.saveImage(applicationContext, "post-image", bitmap, quality)
        imagePath = absolutePath
    }
}

