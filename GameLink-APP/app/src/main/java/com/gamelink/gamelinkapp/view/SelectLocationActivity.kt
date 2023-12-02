package com.gamelink.gamelinkapp.view

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.gamelink.gamelinkapp.R
import com.gamelink.gamelinkapp.databinding.ActivitySelectLocationBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class SelectLocationActivity : AppCompatActivity() {
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var binding: ActivitySelectLocationBinding
    private var latLngSelectedPosition: LatLng? = null


    // private lateinit var viewModel: com.gamelink.gamelinkapp.viewmodel.MapViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySelectLocationBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.buttonClose.setOnClickListener {
            finish()
        }

        binding.buttonSave.setOnClickListener {
            if(latLngSelectedPosition != null) {
                val intent = Intent()
                val bundle = Bundle()
                bundle.putDouble("latitude", latLngSelectedPosition!!.latitude)
                bundle.putDouble("longitude", latLngSelectedPosition!!.longitude)
                intent.putExtras(bundle)

                setResult(Activity.RESULT_OK, intent)
                finish()
            } else {
                finish()
            }

        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        initializeMap()
    }

    private fun initializeMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_map) as SupportMapFragment

        mapFragment.getMapAsync { googleMap ->
            map = googleMap

            map.setOnMapClickListener { point ->
                map.clear()
                addMarkerAtLocation(point)
            }
        }



        checkLocationPermission()
    }

    private fun addMarkerAtLocation(latLng: LatLng) {
        map.addMarker(MarkerOptions().position(latLng).title("Local Selecionada"))
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))

        latLngSelectedPosition = latLng
    }


    private fun checkLocationPermission() {
        val task = fusedLocationProviderClient.lastLocation

        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
            return
        }

        task.addOnSuccessListener { location ->
            if(location != null)
                addMarkerAtLocation(LatLng(location.latitude, location.longitude))
        }


    }
}