package com.gamelink.gamelinkapp.view.createProfile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.gamelink.gamelinkapp.R
import com.gamelink.gamelinkapp.databinding.ActivityCreateProfileStep1Binding

class CreateProfileStep1Activity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityCreateProfileStep1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateProfileStep1Binding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.buttonConfirm.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.button_confirm -> handleConfirm()
        }
    }

    private fun handleConfirm() {
        val intent = Intent(applicationContext, CreateProfileStep2Activity::class.java)
        val bundle = Bundle()
        bundle.putString("name", binding.editName.text.toString())
        intent.putExtras(bundle)
        startActivity(intent)
    }
}