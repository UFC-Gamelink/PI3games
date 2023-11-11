package com.gamelink.gamelinkapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gamelink.gamelinkapp.R
import com.gamelink.gamelinkapp.databinding.ActivityProfileFormBinding

class ProfileFormActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileFormBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileFormBinding.inflate(layoutInflater)

        setContentView(binding.root)


    }
}