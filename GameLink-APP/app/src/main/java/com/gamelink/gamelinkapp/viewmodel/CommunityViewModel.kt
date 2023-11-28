package com.gamelink.gamelinkapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gamelink.gamelinkapp.service.constants.GameLinkConstants
import com.gamelink.gamelinkapp.service.listener.APIListener
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

    fun isOwner(communityId: String) {
        Log.d("IDOWNER", communityId)
        Log.d("IDLGOADO", securityPreferences.get(GameLinkConstants.SHARED.USER_ID))
        val isOwner = communityId == securityPreferences.get(GameLinkConstants.SHARED.USER_ID)

        _userIsOwner.value = isOwner

    }

    fun joined(communityId: String) {
//        val userId = securityPreferences.get(GameLinkConstants.SHARED.USER_ID).toString()
//
//        _joined.value = userCommunityRepository.userIsJoin(userId, communityId)
        _joined.value = false
    }

    fun join(communityId: String) {
        viewModelScope.launch {
            communityRepository.joinCommunity(communityId, object : APIListener<Boolean> {
                override fun onSuccess(result: Boolean) {
                    _joined.value = true
                }

                override fun onFailure(message: String) {
                    _joined.value = false
                }
            })
        }
    }

    fun leave(communityId: String) {
        viewModelScope.launch {
            communityRepository.leaveCommunity(communityId, object : APIListener<Boolean> {
                override fun onSuccess(result: Boolean) {
                    _joined.value = false
                }

                override fun onFailure(message: String) {
                    _joined.value = true
                }

            })

        }
    }

    fun delete(id: String) {
        viewModelScope.launch {
            communityRepository.delete(id, object : APIListener<Boolean> {
                override fun onSuccess(result: Boolean) {
                    _delete.value = ValidationModel()
                }

                override fun onFailure(message: String) {
                    _delete.value = ValidationModel()
                }

            })
        }
    }
}