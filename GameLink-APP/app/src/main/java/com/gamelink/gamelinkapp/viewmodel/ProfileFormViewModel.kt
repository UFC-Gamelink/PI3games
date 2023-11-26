package com.gamelink.gamelinkapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gamelink.gamelinkapp.service.listener.APIListener
import com.gamelink.gamelinkapp.service.model.ProfileModel
import com.gamelink.gamelinkapp.service.model.ValidationModel
import com.gamelink.gamelinkapp.service.repository.ProfileRepository
import com.gamelink.gamelinkapp.service.repository.SecurityPreferences
import com.gamelink.gamelinkapp.service.repository.UserRepository
import kotlinx.coroutines.launch

class ProfileFormViewModel(application: Application) : AndroidViewModel(application) {
    private val profileRepository = ProfileRepository(application.applicationContext)
    private val userRepository = UserRepository(application.applicationContext)
    private val securityPreferences = SecurityPreferences(application.applicationContext)

    private val _profile = MutableLiveData<ProfileModel>()
    val profile: LiveData<ProfileModel> = _profile

    private val _update = MutableLiveData<ValidationModel>()
    val update: LiveData<ValidationModel> = _update

    fun load() {
        viewModelScope.launch {
            _profile.value = profileRepository.getByUser()
        }
    }

    fun save(profile: ProfileModel) {
        viewModelScope.launch {
            profileRepository.update(profile, object : APIListener<Boolean> {
                override fun onSuccess(result: Boolean) {
                    _update.value = ValidationModel()
                }

                override fun onFailure(message: String) {
                    _update.value = ValidationModel(message)
                }
            })
        }
    }
}