package com.gamelink.gamelinkapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gamelink.gamelinkapp.service.constants.GameLinkConstants
import com.gamelink.gamelinkapp.service.model.CommunityModel
import com.gamelink.gamelinkapp.service.model.UserCommunityModel
import com.gamelink.gamelinkapp.service.repository.CommunityRepository
import com.gamelink.gamelinkapp.service.repository.SecurityPreferences
import com.gamelink.gamelinkapp.service.repository.UserCommunityRepository

class CommunityViewModel(application: Application) : AndroidViewModel(application) {
    private val communityRepository = CommunityRepository(application.applicationContext)
    private val userCommunityRepository = UserCommunityRepository(application.applicationContext)
    private val securityPreferences = SecurityPreferences(application.applicationContext)

    private val _community = MutableLiveData<CommunityModel>()
    val community: LiveData<CommunityModel> = _community

    private val _userIsOwner = MutableLiveData<Boolean>()
    val userIsOwner: LiveData<Boolean> = _userIsOwner

    private val _joined = MutableLiveData<Boolean>()
    val joined: LiveData<Boolean> = _joined

    fun load(id: Int) {
        _community.value = communityRepository.getById(id)
    }

    fun isOwner() {
        val isOwner =
            _community.value?.ownerId == securityPreferences.get(GameLinkConstants.SHARED.USER_ID)
                .toInt()

        _userIsOwner.value = isOwner

    }

    fun joined(communityId: Int) {
        val userId = securityPreferences.get(GameLinkConstants.SHARED.USER_ID).toInt()

        _joined.value = userCommunityRepository.userIsJoin(userId, communityId)
    }

    fun join(communityId: Int) {
        val userId = securityPreferences.get(GameLinkConstants.SHARED.USER_ID)

        val userCommunity = UserCommunityModel().apply {
            this.userId = userId.toInt()
            this.communityId = communityId
        }

        userCommunityRepository.joinCommunity(userCommunity)

        _joined.value = true
    }

    fun leave(communityId: Int) {
        val userId = securityPreferences.get(GameLinkConstants.SHARED.USER_ID).toInt()

        userCommunityRepository.leaveCommunity(userId, communityId)

        _joined.value = false
    }
}