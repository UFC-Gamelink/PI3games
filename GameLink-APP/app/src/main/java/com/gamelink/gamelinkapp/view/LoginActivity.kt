package com.gamelink.gamelinkapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.gamelink.gamelinkapp.R
import com.gamelink.gamelinkapp.databinding.ActivityLoginBinding
import com.gamelink.gamelinkapp.view.utils.LoadingDialog
import com.gamelink.gamelinkapp.view.createProfile.CreateProfileStep1Activity
import com.gamelink.gamelinkapp.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        loadingDialog = LoadingDialog(this)

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
        val username = binding.editUsername.text.toString().trim()
        val password = binding.editPassword.text.toString().trim()

        loadingDialog.startLoadingDialog()
        viewModel.login(username, password)
    }

    private fun observe() {
        viewModel.login.observe(this) {
            if(it.status()) {
                viewModel.verifyHasNotProfile()
            } else {
                loadingDialog.dismissDialog()
                Toast.makeText(this, it.message(), Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.loggedUser.observe(this) {
            if(it) {
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            }
        }

        viewModel.hasNotProfile.observe(this) {
            if(it) {
                loadingDialog.dismissDialog()
                startActivity(Intent(applicationContext, CreateProfileStep1Activity::class.java))
                finish()
            } else {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        viewModel.usernameHelperErrorResId.observe(this) {
            loadingDialog.dismissDialog()
            binding.textUsernameHelper.text = if (it != null) getString(it) else ""
        }

        viewModel.usernameFieldErrorResId.observe(this) {
            loadingDialog.dismissDialog()
            binding.editUsername.setBackgroundResource(it)
        }

        viewModel.passwordFieldErrorResId.observe(this) {
            loadingDialog.dismissDialog()
            binding.editPassword.setBackgroundResource(it)
        }

        viewModel.passwordHelperErrorResId.observe(this) {
            loadingDialog.dismissDialog()
            binding.textPasswordHelper.text = if (it != null) getString(it) else ""
        }
    }
}