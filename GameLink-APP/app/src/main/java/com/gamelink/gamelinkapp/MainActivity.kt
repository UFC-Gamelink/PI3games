package com.gamelink.gamelinkapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.gamelink.gamelinkapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)


        binding.textGamelink.setOnClickListener(this)

    }

    override fun onClick(view: View) {
        if(view.id == R.id.text_gamelink) {
            startActivity(Intent(this, RegisterUserActivity::class.java))
        }
    }
}