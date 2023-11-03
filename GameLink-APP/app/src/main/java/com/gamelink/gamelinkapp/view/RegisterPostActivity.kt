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
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.gamelink.gamelinkapp.R
import com.gamelink.gamelinkapp.databinding.ActivityRegisterPostBinding
import com.gamelink.gamelinkapp.service.model.PostModel
import com.gamelink.gamelinkapp.utils.ImageUtils
import com.gamelink.gamelinkapp.view.adapter.ItemAdapter
import com.gamelink.gamelinkapp.viewmodel.RegisterPostViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID

class RegisterPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterPostBinding
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: RegisterPostViewModel
    private lateinit var dialog: AlertDialog
    private var imagePreview: Bitmap? = null
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")

    private val list = ArrayList<String>()

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

                binding.imagePreview.setImageBitmap(bitmap)
                imagePreview = bitmap

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

        for(i in 1..10) {
            list.add("item $i")
        }

        binding.textVisibilityToggle.setOnClickListener {
            showBottomSheet()
        }

        binding.imageAddImage.setOnClickListener {
            checkGalleryPermission()
        }

        observe()

        binding.buttonPost.setOnClickListener { handlePost() }

        binding.imageRemoveImage.setOnClickListener {
            binding.imagePreview.setImageBitmap(null)
            binding.imageRemoveImage.visibility = View.GONE
        }

        setContentView(binding.root)

    }

    private fun showBottomSheet () {
        val dialogView  = layoutInflater.inflate(R.layout.bottom_sheet, null)
        val dialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        dialog.setContentView(dialogView)
        recyclerView = dialogView.findViewById(R.id.recycler_view_item)
        itemAdapter = ItemAdapter(list)
        recyclerView.adapter = itemAdapter
        dialog.show()

    }

    private fun handlePost() {
        var imagePostPath: String? = null
        if(imagePreview != null) {
            imagePostPath = ImageUtils.saveImage(applicationContext, "imagepost_${UUID.randomUUID().toString()}", imagePreview!!)
        }

        val post = PostModel().apply {
            this.post = binding.editPost.text.toString().trim()
            this.postImagePath = imagePostPath
            this.createdAt = dateFormat.format(Date());
        }

        viewModel.save(post)
    }

    private fun observe() {
        viewModel.postSave.observe(this) {
            if(it.status()) {
                Toast.makeText(applicationContext, "salvo com sucesso", Toast.LENGTH_SHORT).show()
                imagePreview = null
                finish()
            } else {
                Toast.makeText(applicationContext, it.message(), Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun checkGalleryPermission() {
        val galleryPermissionAccepted = checkPermission(PERMISSION_GALLERY)

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

}