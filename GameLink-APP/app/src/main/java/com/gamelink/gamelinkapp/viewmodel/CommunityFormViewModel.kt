package com.gamelink.gamelinkapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gamelink.gamelinkapp.service.constants.GameLinkConstants
import com.gamelink.gamelinkapp.service.listener.APIListener
import com.gamelink.gamelinkapp.service.model.CommunityModel
import com.gamelink.gamelinkapp.service.model.ValidationModel
import com.gamelink.gamelinkapp.service.repository.CommunityRepository
import com.gamelink.gamelinkapp.service.repository.SecurityPreferences
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class CommunityFormViewModel(application: Application) : AndroidViewModel(application) {
    private val communityRepository = CommunityRepository(application.applicationContext)
    private val securityPreferences = SecurityPreferences(application.applicationContext)

    private val _communitySave = MutableLiveData<ValidationModel>()
    val communitySave: LiveData<ValidationModel> = _communitySave

    private val _community = MutableLiveData<CommunityModel>()
    val community: LiveData<CommunityModel> = _community

    private val _idCommunity = MutableLiveData<String>()
    val idCommunity: LiveData<String> = _idCommunity

    private var isFormValid = false

    fun save(community: CommunityModel) {
        isFormValid = true

        getErrorIfEmptyValue(community.name)
        getErrorIfEmptyValue(community.description)

        if (isFormValid) {
            viewModelScope.launch {
                if (community.id == "") {
                    communityRepository.create(community, object : APIListener<CommunityModel> {
                        override fun onSuccess(result: CommunityModel) {
                            if(community.bannerUrl == null) {
                                _communitySave.value = ValidationModel()
                            } else {
                                _idCommunity.value = result.id
                            }
                        }

                        override fun onFailure(message: String) {
                            _communitySave.value = ValidationModel(message)
                        }

                    })
                } else {
                    communityRepository.update(community)
                }
            }
        }
    }

    fun saveBanner(community: CommunityModel) {
        viewModelScope.launch {
            val bannerFile = File(community.bannerUrl!!)
            val requestBannerFile = RequestBody.create(MediaType.parse("image/*"), bannerFile)
            val iconPart = MultipartBody.Part.createFormData("banner", bannerFile.name, requestBannerFile)

            communityRepository.saveBanner(community.id, iconPart, object :APIListener<Boolean> {
                override fun onSuccess(result: Boolean) {
                    _communitySave.value = ValidationModel()
                }

                override fun onFailure(message: String) {
                    _communitySave.value = ValidationModel(message)
                }

            })
        }
    }

    fun load(communityId: String) {
        viewModelScope.launch {
            _community.value = communityRepository.getById(communityId)
        }

    }

    private fun getErrorIfEmptyValue(value: String) {
        if (value.isEmpty()) {
            isFormValid = false
            _communitySave.value = ValidationModel("Preencha todos os campos")
        }
    }
}