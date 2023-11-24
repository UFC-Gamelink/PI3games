package com.gamelink.gamelinkapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gamelink.gamelinkapp.service.constants.GameLinkConstants
import com.gamelink.gamelinkapp.service.model.ProfileModel
import com.gamelink.gamelinkapp.service.model.UserAndProfileModel
import com.gamelink.gamelinkapp.service.model.UserModel
import com.gamelink.gamelinkapp.service.model.ValidationModel
import com.gamelink.gamelinkapp.service.repository.ProfileRepository
import com.gamelink.gamelinkapp.service.repository.SecurityPreferences
import com.gamelink.gamelinkapp.service.repository.UserRepository

class ProfileFormViewModel(application: Application) : AndroidViewModel(application) {
    private val profileRepository = ProfileRepository(application.applicationContext)
    private val userRepository = UserRepository(application.applicationContext)
    private val securityPreferences = SecurityPreferences(application.applicationContext)

    private val _userAndProfile = MutableLiveData<UserAndProfileModel>()
    val userAndProfile: LiveData<UserAndProfileModel> = _userAndProfile

    private val _update = MutableLiveData<ValidationModel>()
    val update: LiveData<ValidationModel> = _update

    fun load(userId: Int) {
       // _userAndProfile.value = userRepository.getUserAndProfile(userId)
    }

    fun save(user: UserModel, profile: ProfileModel) {
        //userRepository.update(user)
        profileRepository.update(profile)

        //securityPreferences.store(GameLinkConstants.SHARED.USERNAME, user.username!!)

        _update.value = ValidationModel()
    }
}