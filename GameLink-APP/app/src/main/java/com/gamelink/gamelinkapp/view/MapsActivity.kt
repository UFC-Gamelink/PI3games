package com.gamelink.gamelinkapp.view

import MapsViewModel
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.gamelink.gamelinkapp.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.gamelink.gamelinkapp.R
import androidx.lifecycle.ViewModelProvider
import com.gamelink.gamelinkapp.service.model.PostModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.SupportMapFragment

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener {

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var viewModel: MapsViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(MapsViewModel::class.java)

        binding.buttonClose.setOnClickListener {
            finish()
        }

        // Configurar listeners
        binding.buttonSearch.setOnClickListener(this)
        binding.buttonSave.setOnClickListener(this)

        // Inicializar o mapa
        initializeMap()

        // Observar mudanças no ViewModel
        setupObservers()

    }

    private fun initializeMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        // Inicialização do FusedLocationProviderClient
        // Funciona para pegar a atualização atual da pessoa
        getClientLocation()
    }

    private fun getClientLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    addMarkerAtLocation(location)
                }
            }
    }

    private fun setupObservers() {
        viewModel.location.observe(this) { location ->
            addMarkerAtLocation(location)
        }

        viewModel.address.observe(this) { address ->
            // Atualizar a UI com o endereço, se necessário
        }

        viewModel.postSave.observe(this) {
            if (it.status()) {
                Toast.makeText(applicationContext, "Salvo com sucesso", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(applicationContext, it.message(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
    }

    override fun onClick(v: View) {

        if (binding.editLocal.text.toString() != "") when (v.id) {
            R.id.button_search -> {
                viewModel.searchAddress(binding.editLocal.text.toString())
            }

            R.id.button_save -> {
                handleSave()
            }

        } else when (v.id) {
            R.id.button_search -> {
                Toast.makeText(this, getString(R.string.local_not_found), Toast.LENGTH_SHORT).show()
            }

            R.id.button_save -> {
                Toast.makeText(this, "Por favor informe o local", Toast.LENGTH_SHORT).show()
            }
        }

    }


    private fun handleSave() {
        val post = PostModel().apply {
            this.description = binding.editPost.text.toString().trim()

            val local = viewModel.getLocation()

            local?.let { this.latitude = it.latitude }
            local?.let { this.longitude = it.longitude }
        }

        viewModel.save(post)
//        val intent = Intent(this, RegisterPostActivity::class.java).apply {
//            viewModel.searchAddress(binding.editLocal.text.toString())
//            val local = viewModel.getLocation()
//            setupObservers()
//            local?.let { putExtra("EXTRA_LATITUDE", it.latitude) }
//            local?.let { putExtra("EXTRA_LONGITUDE", it.longitude) }
//
//        }
//        startActivity(intent)
    }


    private fun addMarkerAtLocation(location: Location) {
        val latLng = LatLng(location.latitude, location.longitude)
        map.addMarker(MarkerOptions().position(latLng).title("Local do evento"))
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
    }

}


