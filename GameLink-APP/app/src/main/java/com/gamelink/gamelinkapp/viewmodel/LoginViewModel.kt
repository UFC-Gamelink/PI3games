package com.gamelink.gamelinkapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gamelink.gamelinkapp.R
import com.gamelink.gamelinkapp.service.constants.GameLinkConstants
import com.gamelink.gamelinkapp.service.listener.APIListener
import com.gamelink.gamelinkapp.service.model.UserModel
import com.gamelink.gamelinkapp.service.model.ValidationModel
import com.gamelink.gamelinkapp.service.repository.SecurityPreferences
import com.gamelink.gamelinkapp.service.repository.UserRepository
import com.gamelink.gamelinkapp.service.repository.remote.RetrofitClient

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository = UserRepository(application.applicationContext)
    private val securityPreferences = SecurityPreferences(application.applicationContext)

    private val _usernameFieldErrorResId = MutableLiveData<Int>()
    val usernameFieldErrorResId: LiveData<Int> = _usernameFieldErrorResId

    private val _usernameHelperErrorResId = MutableLiveData<Int?>()
    val usernameHelperErrorResId: LiveData<Int?> = _usernameHelperErrorResId

    private val _passwordFieldErrorResId = MutableLiveData<Int>()
    val passwordFieldErrorResId: LiveData<Int> = _passwordFieldErrorResId

    private val _passwordHelperErrorResId = MutableLiveData<Int?>()
    val passwordHelperErrorResId: LiveData<Int?> = _passwordHelperErrorResId

    private val _login = MutableLiveData<ValidationModel>()
    val login: LiveData<ValidationModel> = _login

    private val _loggedUser = MutableLiveData<Boolean>()
    val loggedUser: LiveData<Boolean> = _loggedUser

    private var isFormValid = false

    fun login(username: String, password: String) {
        isFormValid = true

        _usernameFieldErrorResId.value = getDrawableResIdIfNull(username)
        _usernameHelperErrorResId.value = getErrorStringResIdIfEmpty(username)

        _passwordFieldErrorResId.value = getDrawableResIdIfNull(password)
        _passwordHelperErrorResId.value = getErrorStringResIdIfEmpty(password)

        if (isFormValid) {
            userRepository.login(username, password, object : APIListener<UserModel> {
                override fun onSuccess(result: UserModel) {
                     securityPreferences.store(GameLinkConstants.SHARED.TOKEN_KEY, result.token)

                    RetrofitClient.addHeaders(result.token)

                    _login.value = ValidationModel()
                }


                override fun onFailure(message: String) {
                    _login.value = ValidationModel(message)
                }

            })
        }
    }

    fun verifyLoggedUser() {
        val token = securityPreferences.get(GameLinkConstants.SHARED.TOKEN_KEY)

        RetrofitClient.addHeaders(token)

        val logged = token != ""
        _loggedUser.value = logged
    }

    private fun getErrorStringResIdIfEmpty(value: String): Int? {
        return if (value.isEmpty()) {
            isFormValid = false
            R.string.required_field_error
        } else null
    }

    private fun getDrawableResIdIfNull(value: String): Int {
        return if (value.isEmpty()) {
            isFormValid = false
            R.drawable.rounded_edit_text_error
        } else R.drawable.rounded_edit_text
    }
}