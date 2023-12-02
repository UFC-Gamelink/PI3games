package com.gamelink.gamelinkapp.view.createProfile

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.gamelink.gamelinkapp.R
import com.gamelink.gamelinkapp.databinding.ActivityCreateProfileStep4Binding
import com.gamelink.gamelinkapp.utils.ImageUtils
import com.gamelink.gamelinkapp.view.SelectLocationActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class CreateProfileStep4Activity : AppCompatActivity(), View.OnClickListener,
    DatePickerDialog.OnDateSetListener {
    private lateinit var binding: ActivityCreateProfileStep4Binding
    private lateinit var bundle: Bundle
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val activityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if(result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                data?.let {
                    val bundleMap = it.extras!!
                    val latitude = bundleMap.getDouble("latitude")
                    val longitude = bundleMap.getDouble("longitude")

                    bundle.putDouble("latitude", latitude)
                    bundle.putDouble("longitude", longitude)

                    binding.buttonLocation.text = getInfoLocation(latitude, longitude)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateProfileStep4Binding.inflate(layoutInflater)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

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
        binding.buttonLocation.setOnClickListener(this)
    }

    override fun onDateSet(v: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)


        binding.buttonDate.text = SimpleDateFormat("dd/MM/yyyy").format(calendar.time)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.button_date -> handleDate()
            R.id.button_location -> {
                val intent = Intent(this, SelectLocationActivity::class.java)
                activityResultLauncher.launch(intent)
            }
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
        if (binding.buttonDate.text.toString().isEmpty()) {
            Toast.makeText(this, "Selecione uma data", Toast.LENGTH_SHORT).show()
        } else {
            val date = SimpleDateFormat("dd/MM/yyyy").parse(binding.buttonDate.text.toString())

            bundle.putString("birthday", SimpleDateFormat("yyyy-MM-dd").format(date))
            bundle.putBoolean("show_birthday", binding.switchShowDate.isChecked)
            bundle.putBoolean("show_location", binding.switchShowLocation.isChecked)

            startActivity(
                Intent(
                    applicationContext,
                    CreateProfileStep5Activity::class.java
                ).putExtras(bundle)
            )
        }

    }
    private fun getInfoLocation(latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(this, Locale.getDefault())

        var addressList: MutableList<Address> = geocoder.getFromLocation(latitude, longitude, 1)!!

        if (addressList.size != 0) {
            val location: Address = addressList[0]

            return "${location.subAdminArea} - ${location.adminArea}"
        }

        return ""
    }

}