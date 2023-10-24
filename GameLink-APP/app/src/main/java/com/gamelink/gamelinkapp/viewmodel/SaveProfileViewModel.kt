package com.gamelink.gamelinkapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gamelink.gamelinkapp.service.constants.GameLinkConstants
import com.gamelink.gamelinkapp.service.model.ProfileModel
import com.gamelink.gamelinkapp.service.model.ValidationModel
import com.gamelink.gamelinkapp.service.repository.ProfileRepository
import com.gamelink.gamelinkapp.service.repository.SecurityPreferences

class SaveProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val profileRepository = ProfileRepository(application.applicationContext)
    private val securityPreferences = SecurityPreferences(application.applicationContext)

    private val _profileSave = MutableLiveData<ValidationModel>()
    val profileSave: LiveData<ValidationModel> = _profileSave

    fun save(profile: ProfileModel) {
        profile.userId = securityPreferences.get(GameLinkConstants.SHARED.USER_ID).toInt()

        profileRepository.save(profile)
        _profileSave.value = ValidationModel()
    }
}