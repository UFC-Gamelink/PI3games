package com.gamelink.gamelinkapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gamelink.gamelinkapp.service.constants.GameLinkConstants
import com.gamelink.gamelinkapp.service.model.CommunityModel
import com.gamelink.gamelinkapp.service.model.UserCommunityModel
import com.gamelink.gamelinkapp.service.model.ValidationModel
import com.gamelink.gamelinkapp.service.repository.CommunityRepository
import com.gamelink.gamelinkapp.service.repository.PostRepository
import com.gamelink.gamelinkapp.service.repository.SecurityPreferences
import com.gamelink.gamelinkapp.service.repository.UserCommunityRepository
import kotlinx.coroutines.launch

class CommunityViewModel(application: Application) : AndroidViewModel(application) {
    private val communityRepository = CommunityRepository(application.applicationContext)
    private val userCommunityRepository = UserCommunityRepository(application.applicationContext)
    private val postRepository = PostRepository(application.applicationContext)
    private val securityPreferences = SecurityPreferences(application.applicationContext)

    private val _community = MutableLiveData<CommunityModel>()
    val community: LiveData<CommunityModel> = _community

    private val _userIsOwner = MutableLiveData<Boolean>()
    val userIsOwner: LiveData<Boolean> = _userIsOwner

    private val _joined = MutableLiveData<Boolean>()
    val joined: LiveData<Boolean> = _joined

    private val _delete = MutableLiveData<ValidationModel>()
    val delete: LiveData<ValidationModel> = _delete

    fun load(id: String) {
        viewModelScope.launch {
            _community.value = communityRepository.getById(id)
        }
    }

    fun isOwner() {
        val isOwner =
            _community.value?.ownerId == securityPreferences.get(GameLinkConstants.SHARED.USER_ID)

        _userIsOwner.value = isOwner

    }

    fun joined(communityId: String) {
//        val userId = securityPreferences.get(GameLinkConstants.SHARED.USER_ID).toString()
//
//        _joined.value = userCommunityRepository.userIsJoin(userId, communityId)
        _joined.value = false
    }

    fun join(communityId: String) {
        val userId = securityPreferences.get(GameLinkConstants.SHARED.USER_ID)

//        val userCommunity = UserCommunityModel().apply {
//            this.userId = userId
//            this.communityId = communityId
//        }
//
//        userCommunityRepository.joinCommunity(userCommunity)

        _joined.value = true
    }

    fun leave(communityId: String) {
        val userId = securityPreferences.get(GameLinkConstants.SHARED.USER_ID).toString()

        //userCommunityRepository.leaveCommunity(userId, communityId)

        _joined.value = false
    }

    fun delete(id: String) {
//        userCommunityRepository.deleteMembers(id)
//        communityRepository.delete(id)
//        postRepository.deleteFromCommunity(id)

        _delete.value = ValidationModel()
    }
}