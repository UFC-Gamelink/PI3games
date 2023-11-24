package com.gamelink.gamelinkapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gamelink.gamelinkapp.service.constants.GameLinkConstants
import com.gamelink.gamelinkapp.service.listener.APIListener
import com.gamelink.gamelinkapp.service.model.ProfileModel
import com.gamelink.gamelinkapp.service.model.ValidationModel
import com.gamelink.gamelinkapp.service.repository.ProfileRepository
import com.gamelink.gamelinkapp.service.repository.SecurityPreferences
import kotlinx.coroutines.launch

class SaveProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val profileRepository = ProfileRepository(application.applicationContext)
    private val securityPreferences = SecurityPreferences(application.applicationContext)

    private val _profileSave = MutableLiveData<ValidationModel>()
    val profileSave: LiveData<ValidationModel> = _profileSave

    fun save(profile: ProfileModel) {
        //profile.userId = securityPreferences.get(GameLinkConstants.SHARED.USER_ID).toInt()
        viewModelScope.launch {
            profileRepository.save(profile, object : APIListener<Boolean> {
                override fun onSuccess(result: Boolean) {
                    _profileSave.value = ValidationModel()
                }

                override fun onFailure(message: String) {
                    _profileSave.value = ValidationModel(message)
                }
            })
        }
    }
}