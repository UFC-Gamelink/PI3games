package com.gamelink.gamelinkapp.view.createProfile

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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.gamelink.gamelinkapp.R
import com.gamelink.gamelinkapp.databinding.ActivityCreateProfileStep2Binding
import com.gamelink.gamelinkapp.utils.ImageUtils

class CreateProfileStep2Activity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityCreateProfileStep2Binding
    private lateinit var dialog: AlertDialog
    private lateinit var imageView: ImageView
    private lateinit var bundle: Bundle

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

                imageView.setImageBitmap(bitmap)

                saveImage(bitmap)
            }
        }

    companion object {
        private const val PERMISSION_GALLERY = Manifest.permission.READ_EXTERNAL_STORAGE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateProfileStep2Binding.inflate(layoutInflater)

        setContentView(binding.root)

        bundle = intent.extras!!

        binding.textName.text = bundle.getString("name")

        binding.buttonSelectionProfilePicture.setOnClickListener(this)
        binding.buttonSelectionBannerPicture.setOnClickListener(this)
        binding.buttonNext.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.button_selection_profile_picture -> {
                imageView = binding.imageviewProfilePicture
                checkGalleryPermission()
            }

            R.id.button_selection_banner_picture -> {
                imageView = binding.imageviewBannerPicture
                checkGalleryPermission()
            }

            R.id.button_next -> handleNext()
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



    private fun handleNext() {
        startActivity(
            Intent(
                applicationContext,
                CreateProfileStep3Activity::class.java
            ).putExtras(bundle)
        )
    }

    private fun saveImage(bitmap: Bitmap) {
        when(imageView.id) {
            R.id.imageview_profile_picture -> {
                val absolutePath = ImageUtils.saveImage(applicationContext, "profile_picture", bitmap)
                bundle.putString("profile_picture_path", absolutePath)
            }
            R.id.imageview_banner_picture -> {
                val absolutePath = ImageUtils.saveImage(applicationContext, "banner_picture", bitmap)
                bundle.putString("profile_banner_path", absolutePath)
            }
        }
    }
}