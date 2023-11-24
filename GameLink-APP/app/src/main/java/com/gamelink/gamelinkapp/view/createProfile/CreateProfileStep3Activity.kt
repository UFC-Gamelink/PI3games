package com.gamelink.gamelinkapp.view.createProfile

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.gamelink.gamelinkapp.R

import com.gamelink.gamelinkapp.databinding.ActivityCreateProfileStep3Binding
import com.gamelink.gamelinkapp.utils.ImageUtils

class CreateProfileStep3Activity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityCreateProfileStep3Binding
    private lateinit var bundle: Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateProfileStep3Binding.inflate(layoutInflater)

        setContentView(binding.root)

        bundle = intent.extras!!

        binding.textName.text = bundle.getString("name")

        if (bundle.getString("profile_picture_path") != null) {
            val path = bundle.getString("profile_picture_path")!!
            val bitmap = ImageUtils.getBitmap(path)
            binding.imageviewProfilePicture.setImageBitmap(bitmap)
        }

        if (bundle.getString("profile_banner_path") != null) {
            val path = bundle.getString("profile_banner_path")!!
            val bitmap = ImageUtils.getBitmap(path)
            binding.imageviewBannerPicture.setImageBitmap(bitmap)
        }

        binding.buttonNext.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.button_next -> handleNext()
        }
    }

    private fun handleNext() {
        bundle.putString("bio_profile", binding.edittextBio.text.toString())

        startActivity(
            Intent(
                applicationContext,
                CreateProfileStep4Activity::class.java
            ).putExtras(bundle)
        )
    }
}