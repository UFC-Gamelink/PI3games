package com.gamelink.gamelinkapp.view

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.gamelink.gamelinkapp.R
import com.gamelink.gamelinkapp.databinding.ActivityProfileFormBinding
import com.gamelink.gamelinkapp.service.model.ProfileModel
import com.gamelink.gamelinkapp.utils.ImageUtils
import com.gamelink.gamelinkapp.viewmodel.ProfileFormViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ProfileFormActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileFormBinding
    private lateinit var viewModel: ProfileFormViewModel
    private lateinit var bundle: Bundle
    private var profile: ProfileModel? = null
    private lateinit var dialog: AlertDialog
    private lateinit var imageView: ImageView
    private var iconPreview: Bitmap? = null
    private var bannerPreview: Bitmap? = null
    private var iconPath: String? = null
    private var bannerPath: String? = null


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

                if (imageView.id == R.id.image_profile_picture) {
                    iconPreview = bitmap
                } else if (imageView.id == R.id.image_banner_picture) {
                    bannerPreview = bitmap
                }

                imageView.setImageBitmap(bitmap)
            }
        }

    companion object {
        private const val PERMISSION_GALLERY = Manifest.permission.READ_EXTERNAL_STORAGE
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(ProfileFormViewModel::class.java)
        binding = ActivityProfileFormBinding.inflate(layoutInflater)

        setContentView(binding.root)

        bundle = intent.extras!!

        loadDataFromActivity()

        binding.imageBack.setOnClickListener {
            finish()
        }

        binding.buttonDate.setOnClickListener {
            handleDate()
        }

        binding.buttonSave.setOnClickListener {
            handleSave()
        }

        binding.imageProfilePicture.setOnClickListener {
            imageView = binding.imageProfilePicture
            checkGalleryPermission()
        }

        binding.imageBannerPicture.setOnClickListener {
            imageView = binding.imageBannerPicture
            checkGalleryPermission()
        }

        observe()
    }

    private fun loadDataFromActivity() {
        viewModel.load()
    }

    private fun handleDate() {
        val listener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            val dueDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)
            binding.buttonDate.text = dueDate
        }

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, listener, year, month, day).show()
    }

    private fun handleSave() {
        val profile = profile!!.apply {
            this.name = binding.editNickname.text.toString()
            this.bio = binding.editBio.text.toString()

            val date = SimpleDateFormat(
                "dd/MM/yyyy",
                Locale.getDefault()
            ).parse(binding.buttonDate.text.toString())

            this.birthday = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date!!)
            this.showBirthday = binding.switchShowDate.isChecked

            iconPreview?.let { saveImage("icon", it) }
            bannerPreview?.let { saveImage("banner", it) }

            this.profilePicPath = iconPath
            this.bannerPicPath = bannerPath
        }

        viewModel.update(profile)
    }

    private fun observe() {
        viewModel.profile.observe(this) {
            profile = it

            binding.editNickname.setText(it.name)
            binding.editBio.setText(it.bio)

            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(it.birthday)
            binding.buttonDate.text =
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date!!)
            it.showBirthday = binding.switchShowDate.isChecked

            Glide.with(this).load(it.icon.url).into(binding.imageProfilePicture)
            Glide.with(this).load(it.banner.url).into(binding.imageBannerPicture)
            binding.imageBannerPicture.scaleType = ImageView.ScaleType.CENTER_CROP
        }

        viewModel.update.observe(this) {
            if (it.status()) {

                Toast.makeText(this, "Atualizado", Toast.LENGTH_SHORT).show()
                finish()
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

    private fun saveImage(imageType: String, bitmap: Bitmap) {
        val filename = "${imageType}-image"

        if (imageType == "icon") {
            val absolutePath = ImageUtils.saveImage(applicationContext, filename, bitmap)
            iconPath = absolutePath
        } else if (imageType == "banner") {
            val absolutePath = ImageUtils.saveImage(applicationContext, filename, bitmap)
            bannerPath = absolutePath
        }

    }
}