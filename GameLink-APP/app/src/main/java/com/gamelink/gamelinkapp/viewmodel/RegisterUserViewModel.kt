package com.gamelink.gamelinkapp.viewmodel

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gamelink.gamelinkapp.R
import com.gamelink.gamelinkapp.service.listener.APIListener
import com.gamelink.gamelinkapp.service.model.UserModel
import com.gamelink.gamelinkapp.service.model.ValidationModel
import com.gamelink.gamelinkapp.service.repository.SecurityPreferences
import com.gamelink.gamelinkapp.service.repository.UserRepository
import com.gamelink.gamelinkapp.service.repository.remote.RetrofitClient
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class RegisterUserViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository = UserRepository(application.applicationContext)
    private val securityPreferences = SecurityPreferences(application.applicationContext)

    private val _user = MutableLiveData<ValidationModel>()
    val user: LiveData<ValidationModel> = _user

    private val _usernameFieldErrorResId = MutableLiveData<Int>()
    val usernameFieldErrorResId: LiveData<Int> = _usernameFieldErrorResId

    private val _usernameHelperErrorResId = MutableLiveData<Int?>()
    val usernameHelperErrorResId: LiveData<Int?> = _usernameHelperErrorResId

    private val _emailFieldErrorResId = MutableLiveData<Int>()
    val emailFieldErrorResId: LiveData<Int> = _emailFieldErrorResId

    private val _emailHelperErrorResId = MutableLiveData<Int?>()
    val emailHelperErrorResId: LiveData<Int?> = _emailHelperErrorResId

    private val _passwordFieldErrorResId = MutableLiveData<Int>()
    val passwordFieldErrorResId: LiveData<Int> = _passwordFieldErrorResId

    private val _passwordHelperErrorResId = MutableLiveData<Int?>()
    val passwordHelperErrorResId: LiveData<Int?> = _passwordHelperErrorResId


    private var isFormValid = false

    fun create(username: String, email: String, password: String) {
        isFormValid = true

        _usernameFieldErrorResId.value = getDrawableResIdIfNull(username)
        _usernameHelperErrorResId.value = getErrorStringResIdIfEmpty(username)

        _emailFieldErrorResId.value = getDrawableResIdIfInvalidEmail(email)
        _emailHelperErrorResId.value = getErrorStringResIdIfInvalidEmail(email)

        _passwordFieldErrorResId.value = getDrawableResIdIfInvalidPassword(password)
        _passwordHelperErrorResId.value = getErrorStringResIdIfInvalidPassword(password)

        if(isFormValid) {
            viewModelScope.launch {
                val user = UserModel().apply {
                    this.username = username
                    this.email = email
                    this.password = password
                }

                userRepository.create(user, object : APIListener<Boolean> {
                    override fun onSuccess(result: Boolean) {
                        _user.value = ValidationModel()
                    }

                    override fun onFailure(message: String) {
                        _user.value = ValidationModel(message)
                    }

                })
            }

        }
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

    private fun getErrorStringResIdIfInvalidEmail(value: String): Int? {
        return if(!Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            isFormValid = false
            R.string.invalid_email_error
        } else null
    }

    private fun getDrawableResIdIfInvalidEmail(value: String): Int? {
        return if(!Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            isFormValid = false
            R.drawable.rounded_edit_text_error
        } else R.drawable.rounded_edit_text
    }

    private fun getErrorStringResIdIfInvalidPassword(value: String): Int? {
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"

        return if(!Pattern.compile(passwordPattern).matcher(value).matches()) {
            isFormValid = false
            R.string.invalid_password_error
        } else null
    }

    private fun getDrawableResIdIfInvalidPassword(value: String): Int {
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"

        return if(!Pattern.compile(passwordPattern).matcher(value).matches()) {
            isFormValid = false
            R.drawable.rounded_edit_text_error
        } else R.drawable.rounded_edit_text
    }
}