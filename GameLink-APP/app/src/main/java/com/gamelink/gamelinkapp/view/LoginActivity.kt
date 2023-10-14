package com.gamelink.gamelinkapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.gamelink.gamelinkapp.R
import com.gamelink.gamelinkapp.databinding.ActivityLoginBinding
import com.gamelink.gamelinkapp.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.textRegister.setOnClickListener(this)
        binding.buttonLogin.setOnClickListener(this)

        viewModel.verifyLoggedUser()

        observe()
    }

    override fun onClick(view: View) {
        if(view.id == R.id.text_register) {
            startActivity(Intent(this, RegisterUserActivity::class.java))
        } else if(view.id == R.id.button_login) {
            handleLogin()
        }
    }

    private fun handleLogin() {
//        val username = binding.editUsername.text.toString().trim()
//        val password = binding.editPassword.text.toString().trim()
//
//
//        viewModel.login(username, password)
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun observe() {
        viewModel.login.observe(this) {
            if(it.status()) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, it.message(), Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.loggedUser.observe(this) {
            if(it) {
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            }
        }

        viewModel.usernameHelperErrorResId.observe(this) {
            binding.textUsernameHelper.text = if (it != null) getString(it) else ""
        }

        viewModel.usernameFieldErrorResId.observe(this) {
            binding.editUsername.setBackgroundResource(it)
        }

        viewModel.passwordFieldErrorResId.observe(this) {
            binding.editPassword.setBackgroundResource(it)
        }

        viewModel.passwordHelperErrorResId.observe(this) {
            binding.textPasswordHelper.text = if (it != null) getString(it) else ""
        }
    }
}