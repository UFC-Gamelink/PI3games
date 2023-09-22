package com.gamelink.gamelinkapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.gamelink.gamelinkapp.R
import com.gamelink.gamelinkapp.databinding.ActivityRegisterUserBinding
import com.gamelink.gamelinkapp.viewmodel.RegisterUserViewModel

class RegisterUserActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var viewModel: RegisterUserViewModel
    private lateinit var binding: ActivityRegisterUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(RegisterUserViewModel::class.java)
        binding = ActivityRegisterUserBinding.inflate(layoutInflater)

        binding.buttonRegister.setOnClickListener(this)

        binding.editPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                checkPasswordRequirements()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        observe()

        setContentView(binding.root)
    }

    override fun onClick(view: View) {
        if (view.id == R.id.button_register) {
            handleRegister()
        }
    }

    private fun observe() {
        viewModel.usernameFieldErrorResId.observe(this) {
            binding.editUsername.setBackgroundResource(it)
        }

        viewModel.usernameHelperErrorResId.observe(this) {
            binding.textUsernameHelper.text = if (it != null) getString(it) else ""
        }

        viewModel.emailFieldErrorResId.observe(this) {
            binding.editEmail.setBackgroundResource(it)
        }

        viewModel.emailHelperErrorResId.observe(this) {
            binding.textEmailHelper.text = if (it != null) getString(it) else ""
        }

        viewModel.passwordFieldErrorResId.observe(this) {
            binding.editPassword.setBackgroundResource(it)
        }

        viewModel.passwordHelperErrorResId.observe(this) {
            binding.textPasswordHelper.text = if (it != null) getString(it) else ""
        }


        viewModel.user.observe(this) {
            if (it.status()) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, it.message(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleRegister() {
        val username = binding.editUsername.text.toString()
        val email = binding.editEmail.text.toString()
        val password = binding.editPassword.text.toString()

        viewModel.create(username, email, password)
    }

    private fun checkPasswordRequirements() {
        binding.textMinPassword.setTextColor(ContextCompat.getColor(this, R.color.lightgray))
        binding.textUppercase.setTextColor(ContextCompat.getColor(this, R.color.lightgray))
        binding.textLowercase.setTextColor(ContextCompat.getColor(this, R.color.lightgray))
        binding.textNumber.setTextColor(ContextCompat.getColor(this, R.color.lightgray))
        binding.textSpecialCharacter.setTextColor(ContextCompat.getColor(this, R.color.lightgray))

        val passwordText = binding.editPassword.text.toString()

        if (passwordText.length >= 8) {
            binding.textMinPassword.setTextColor(ContextCompat.getColor(this, R.color.blue))
        }

        if (passwordText.contains(".*[A-Z].*".toRegex())) {
            binding.textUppercase.setTextColor(ContextCompat.getColor(this, R.color.blue))
        }

        if (passwordText.contains(".*[a-z].*".toRegex())) {
            binding.textLowercase.setTextColor(ContextCompat.getColor(this, R.color.blue))
        }

        if (passwordText.contains(".*[0-9].*".toRegex())) {
            binding.textNumber.setTextColor(ContextCompat.getColor(this, R.color.blue))
        }

        if (passwordText.contains(".*[@#\$%^&+=].*".toRegex())) {
            binding.textSpecialCharacter.setTextColor(ContextCompat.getColor(this, R.color.blue))
        }
    }


}