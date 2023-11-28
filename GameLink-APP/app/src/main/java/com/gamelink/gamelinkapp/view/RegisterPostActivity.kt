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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gamelink.gamelinkapp.R
import com.gamelink.gamelinkapp.databinding.ActivityRegisterPostBinding
import com.gamelink.gamelinkapp.service.model.CommunityModel
import com.gamelink.gamelinkapp.service.model.PostModel
import com.gamelink.gamelinkapp.utils.ImageUtils
import com.gamelink.gamelinkapp.view.adapter.ItemAdapter
import com.gamelink.gamelinkapp.view.createProfile.CreateProfileStep2Activity
import com.gamelink.gamelinkapp.viewmodel.RegisterPostViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID

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
        private const val PERMISSION_GALLERY = Manifest.permission.READ_EXTERNAL_STORAGE
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        viewModel = ViewModelProvider(this).get(RegisterPostViewModel::class.java)

        binding = ActivityRegisterPostBinding.inflate(layoutInflater)

        binding.buttonClose.setOnClickListener {
            finish()
        }

        binding.imageAddImage.setOnClickListener {
            checkGalleryPermission()
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

        spinnerType()

        val latitude = intent.getDoubleExtra("EXTRA_LATITUDE", 0.0)
        val longitude = intent.getDoubleExtra("EXTRA_LONGITUDE", 0.0)

        viewModel.setCoordenadas(latitude,longitude)



    }

    private fun handlePost() {
        imagePreview?.let { saveImage(it) }

        val post = PostModel().apply {
            this.description = binding.editPost.text.toString().trim()
            this.imageUrl = imagePath
            this.latitude = latitude
            this.longitude = longitude
        }

        viewModel.save(post)
    }

    private fun observe() {
        viewModel.postSave.observe(this) {
            if (it.status()) {
                Toast.makeText(applicationContext, "salvo com sucesso", Toast.LENGTH_SHORT).show()
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

            list.add("publico")
            for (c in it) {
                list.add(c.name)
            }

            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list)
            binding.spinnerVisibility.adapter = adapter
        }

    }

    private fun spinnerType(){

        val spinner = binding.spinnerType
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, viewModel.loadTypePostModel() )
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                if (selectedItem == "Evento") {
                    startActivity(Intent(this@RegisterPostActivity,MapsActivity::class.java))
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

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
        val absolutePath = ImageUtils.saveImage(applicationContext, "post-image", bitmap)
        imagePath = absolutePath
    }
}

