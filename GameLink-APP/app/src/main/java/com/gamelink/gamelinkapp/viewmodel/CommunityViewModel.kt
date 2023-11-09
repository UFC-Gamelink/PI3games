package com.gamelink.gamelinkapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gamelink.gamelinkapp.service.constants.GameLinkConstants
import com.gamelink.gamelinkapp.service.model.CommunityModel
import com.gamelink.gamelinkapp.service.repository.CommunityRepository
import com.gamelink.gamelinkapp.service.repository.SecurityPreferences

class CommunityViewModel(application: Application) : AndroidViewModel(application) {
    private val communityRepository = CommunityRepository(application.applicationContext)
    private val securityPreferences = SecurityPreferences(application.applicationContext)

    private val _community = MutableLiveData<CommunityModel>()
    val community: LiveData<CommunityModel> = _community

    private val _userIsOwner = MutableLiveData<Boolean>()
    val userIsOwner: LiveData<Boolean> = _userIsOwner

    fun load(id: Int) {
        _community.value = communityRepository.getById(id)
    }

    fun isOwner() {
        _userIsOwner.value =
            _community.value?.ownerId == securityPreferences.get(GameLinkConstants.SHARED.USER_ID)
                .toInt()
    }

}