package com.gamelink.gamelinkapp.view.createProfile

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import com.gamelink.gamelinkapp.R
import com.gamelink.gamelinkapp.databinding.ActivityCreateProfileStep4Binding
import com.gamelink.gamelinkapp.utils.ImageUtils
import com.gamelink.gamelinkapp.view.MainActivity
import java.text.SimpleDateFormat
import java.util.Calendar

class CreateProfileStep4Activity : AppCompatActivity(), View.OnClickListener,
    DatePickerDialog.OnDateSetListener {
    private lateinit var binding: ActivityCreateProfileStep4Binding
    private lateinit var bundle: Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateProfileStep4Binding.inflate(layoutInflater)

        setContentView(binding.root)

        bundle = intent.extras!!

        binding.textName.text = bundle.getString("name")
        binding.textBio.text = bundle.getString("bio_profile")

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

        binding.buttonDate.setOnClickListener(this)
        binding.buttonNext.setOnClickListener(this)
    }

    override fun onDateSet(v: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)


        binding.buttonDate.text = SimpleDateFormat("dd/MM/yyyy").format(calendar.time)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.button_date -> handleDate()
            R.id.button_next -> handleNext()
        }
    }

    private fun handleDate() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, this, year, month, day).show()
    }

    private fun handleNext() {
        if(binding.buttonDate.text.toString().isEmpty()) {
            Toast.makeText(this, "Selecione uma data", Toast.LENGTH_SHORT).show()
        }else {
            val date = SimpleDateFormat("dd/MM/yyyy").parse(binding.buttonDate.text.toString())

            bundle.putString("birthday", SimpleDateFormat("yyyy-MM-dd").format(date))
            bundle.putBoolean("show_birthday", binding.switchShowDate.isChecked)

            startActivity(
                Intent(
                    applicationContext,
                    CreateProfileStep5Activity::class.java
                ).putExtras(bundle)
            )
        }

    }

    private fun getBitmap(absolutePath: String): Bitmap {
        return BitmapFactory.decodeFile(absolutePath)

    }
}