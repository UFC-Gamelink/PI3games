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
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class ProfileFormViewModel(application: Application) : AndroidViewModel(application) {
    private val profileRepository = ProfileRepository(application.applicationContext)

    private val _profile = MutableLiveData<ProfileModel>()
    val profile: LiveData<ProfileModel> = _profile

    private val _update = MutableLiveData<ValidationModel>()
    val update: LiveData<ValidationModel> = _update

    fun load() {
        viewModelScope.launch {
            _profile.value = profileRepository.getByUser()
        }
    }

    fun update(profile: ProfileModel) {
        viewModelScope.launch {
            profileRepository.update(profile, object : APIListener<Boolean> {
                override fun onSuccess(result: Boolean) {
                    if (!(profile.profilePicPath == null && profile.bannerPicPath == null)) {
                        updateImages(profile)
                    } else {
                        _update.value = ValidationModel()
                    }

                }

                override fun onFailure(message: String) {
                    _update.value = ValidationModel(message)
                }
            })
        }
    }

    fun updateImages(profile: ProfileModel) {

        viewModelScope.launch {

            var iconPart: MultipartBody.Part? = null
            var bannerPart: MultipartBody.Part? = null

            if (profile.profilePicPath != null) {
                val iconFile = File(profile.profilePicPath!!)
                val requestIconFile = RequestBody.create(MediaType.parse("image/*"), iconFile)
                iconPart =
                    MultipartBody.Part.createFormData("icon", iconFile.name, requestIconFile)
            }

            if (profile.bannerPicPath != null) {
                val bannerFile = File(profile.bannerPicPath!!)
                val requestBannerFile =
                    RequestBody.create(MediaType.parse("image/*"), bannerFile)
                bannerPart =
                    MultipartBody.Part.createFormData(
                        "banner",
                        bannerFile.name,
                        requestBannerFile
                    )
            }

            profileRepository.updateImages(
                iconPart,
                bannerPart,
                object : APIListener<ProfileModel> {
                    override fun onSuccess(result: ProfileModel) {
                        _update.value = ValidationModel()
                    }

                    override fun onFailure(message: String) {
                        _update.value = ValidationModel(message)
                    }
                })

        }

    }
}