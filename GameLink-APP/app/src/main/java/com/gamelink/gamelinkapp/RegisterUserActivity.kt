package com.gamelink.gamelinkapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.gamelink.gamelinkapp.databinding.ActivityRegisterUserBinding

class RegisterUserActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityRegisterUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterUserBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.buttonRegister.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if(view.id == R.id.button_register) {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}