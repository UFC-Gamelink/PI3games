package com.gamelink.gamelinkapp.view

import android.app.DatePickerDialog
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
import com.gamelink.gamelinkapp.R
import com.gamelink.gamelinkapp.databinding.ActivityProfileFormBinding
import com.gamelink.gamelinkapp.service.constants.GameLinkConstants
import com.gamelink.gamelinkapp.service.model.UserAndProfileModel
import com.gamelink.gamelinkapp.utils.ImageUtils
import com.gamelink.gamelinkapp.viewmodel.ProfileFormViewModel
import java.text.SimpleDateFormat
import java.util.Calendar

class ProfileFormActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileFormBinding
    private lateinit var viewModel: ProfileFormViewModel
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy")
    private lateinit var bundle: Bundle
    private var userProfile: UserAndProfileModel? = null
    private lateinit var imageView: ImageView
    private var imageUri: Uri? = null

    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            result.data?.data.let { uri ->
                if (uri != null) {
                    imageUri = uri
                    Glide.with(this).load(uri).into(imageView)

                    if (imageView.id == R.id.image_profile_picture) {
                        bundle.putString("profile_uri", uri.toString())
                    } else if (imageView.id == R.id.image_banner_picture) {
                        bundle.putString("banner_uri", uri.toString())
                    }
                }
            }
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
            chooseImage()
        }

        binding.imageBannerPicture.setOnClickListener {
            imageView = binding.imageBannerPicture
            chooseImage()
        }

        observe()
    }

    private fun loadDataFromActivity() {
        val userId = bundle.getInt(GameLinkConstants.SHARED.USER_ID)

        viewModel.load(userId)
    }

    private fun handleDate() {
        val listener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)

            val dueDate = dateFormat.format(calendar.time)
            binding.buttonDate.text = dueDate
        }

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, listener, year, month, day).show()
    }

    private fun handleSave() {
        val user = userProfile!!.user.apply {
            this.username = binding.editUsername.text.toString()
        }

        val profile = userProfile!!.profile.apply {
            this.name = binding.editNickname.text.toString()
            this.bio = binding.editBio.text.toString()
            this.birthday = binding.buttonDate.text.toString()
            this.showBirthday = binding.switchShowDate.isChecked

            if (bundle.getString("profile_uri") != null) {
                this.profilePicPath =
                    ImageUtils.saveImageUri(
                        applicationContext,
                        Uri.parse(bundle.getString("profile_uri"))
                    )
            }

            if (bundle.getString("banner_uri") != null) {
                this.bannerPicPath =
                    ImageUtils.saveImageUri(
                        applicationContext,
                        Uri.parse(bundle.getString("banner_uri"))
                    )
            }
        }

        viewModel.save(user, profile)
    }

    private fun observe() {
        viewModel.userAndProfile.observe(this) {
            userProfile = it
            binding.editUsername.setText(it.user.username)
            binding.editNickname.setText(it.profile.name)
            binding.editBio.setText(it.profile.bio)

            binding.buttonDate.text = it.profile.birthday

            Glide.with(this).load(it.profile.profilePicPath).into(binding.imageProfilePicture)
            Glide.with(this).load(it.profile.bannerPicPath).into(binding.imageBannerPicture)

            binding.switchShowDate.isChecked = it.profile.showBirthday
        }

        viewModel.update.observe(this) {
            if (it.status()) {
                Toast.makeText(this, "Atualizado", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun chooseImage() {
        getContent.launch(
            Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
        )
    }
}