package com.gamelink.gamelinkapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gamelink.gamelinkapp.service.constants.GameLinkConstants
import com.gamelink.gamelinkapp.service.model.CommunityModel
import com.gamelink.gamelinkapp.service.model.ValidationModel
import com.gamelink.gamelinkapp.service.repository.CommunityRepository
import com.gamelink.gamelinkapp.service.repository.SecurityPreferences

class CommunityFormViewModel(application: Application) : AndroidViewModel(application) {
    private val communityRepository = CommunityRepository(application.applicationContext)
    private val securityPreferences = SecurityPreferences(application.applicationContext)

    private val _communitySave = MutableLiveData<ValidationModel>()
    val communitySave: LiveData<ValidationModel> = _communitySave

    private val _community = MutableLiveData<CommunityModel>()
    val community: LiveData<CommunityModel> = _community

    private var isFormValid = false

    fun save(community: CommunityModel) {
        isFormValid = true

        getErrorIfEmptyValue(community.name)
        getErrorIfEmptyValue(community.description)

        if(isFormValid)  {
            community.ownerId = securityPreferences.get(GameLinkConstants.SHARED.USER_ID).toInt()

            if(community.id == 0) {
                communityRepository.create(community)
            } else {
                communityRepository.update(community)
            }

            _communitySave.value = ValidationModel()
        }
    }

    fun load(communityId: Int) {
        _community.value = communityRepository.getById(communityId)
    }

    private fun getErrorIfEmptyValue(value: String) {
        if(value.isEmpty()) {
            isFormValid = false
            _communitySave.value = ValidationModel("Preencha todos os campos")
        }
    }
}