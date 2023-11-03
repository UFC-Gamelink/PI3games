package com.gamelink.gamelinkapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gamelink.gamelinkapp.service.constants.GameLinkConstants
import com.gamelink.gamelinkapp.service.model.ProfileModel
import com.gamelink.gamelinkapp.service.repository.ProfileRepository
import com.gamelink.gamelinkapp.service.repository.SecurityPreferences

class ProfileViewModel(application: Application)  : AndroidViewModel(application) {
    private val profileRepository = ProfileRepository(application.applicationContext)
    private val securityPreferences = SecurityPreferences(application.applicationContext)

    private val _profile = MutableLiveData<ProfileModel>()
    val profile: LiveData<ProfileModel> = _profile

    fun getProfile() {
        val userId = securityPreferences.get(GameLinkConstants.SHARED.USER_ID)

        val profileModel = profileRepository.getByUser(userId.toInt())

        profileModel!!.username = securityPreferences.get(GameLinkConstants.SHARED.USERNAME)

        _profile.value = profileModel
    }

    fun logout() {
        securityPreferences.remove(GameLinkConstants.SHARED.USER_ID)
        securityPreferences.remove(GameLinkConstants.SHARED.USERNAME)
    }
}