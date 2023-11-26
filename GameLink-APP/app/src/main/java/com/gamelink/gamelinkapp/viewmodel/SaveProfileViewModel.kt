package com.gamelink.gamelinkapp.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
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
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class SaveProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val profileRepository = ProfileRepository(application.applicationContext)

    private val _profileSave = MutableLiveData<ValidationModel>()
    val profileSave: LiveData<ValidationModel> = _profileSave

    private val _profileImagesSave = MutableLiveData<ValidationModel>()
    val profileImagesSave: LiveData<ValidationModel> = _profileImagesSave

    fun save(profile: ProfileModel) {
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

    fun saveImages(profile: ProfileModel) {
        viewModelScope.launch {
            val iconFile = File(profile.profilePicPath!!)

            val bannerFile = File(profile.bannerPicPath!!)

            val requestIconFile = RequestBody.create(MediaType.parse("image/*"), iconFile)
            val requestBannerFile = RequestBody.create(MediaType.parse("image/*"), bannerFile)

            val iconPart = MultipartBody.Part.createFormData("icon", iconFile.name, requestIconFile)
            val bannerPart =
                MultipartBody.Part.createFormData("banner", bannerFile.name, requestBannerFile)

            profileRepository.saveImages(
                iconPart,
                bannerPart,
                object : APIListener<ProfileModel> {
                    override fun onSuccess(result: ProfileModel) {
                        _profileImagesSave.value = ValidationModel()
                    }

                    override fun onFailure(message: String) {
                        _profileImagesSave.value = ValidationModel(message)
                    }

                })
        }

    }
}