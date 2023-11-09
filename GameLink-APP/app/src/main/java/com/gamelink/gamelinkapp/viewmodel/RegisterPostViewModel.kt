package com.gamelink.gamelinkapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gamelink.gamelinkapp.service.constants.GameLinkConstants
import com.gamelink.gamelinkapp.service.model.CommunityModel
import com.gamelink.gamelinkapp.service.model.PostModel
import com.gamelink.gamelinkapp.service.model.ValidationModel
import com.gamelink.gamelinkapp.service.repository.CommunityRepository
import com.gamelink.gamelinkapp.service.repository.PostRepository
import com.gamelink.gamelinkapp.service.repository.SecurityPreferences

class RegisterPostViewModel(application: Application)  : AndroidViewModel(application) {
    private val postRepository = PostRepository(application.applicationContext)
    private val communityRepository = CommunityRepository(application.applicationContext)
    private val securityPreferences = SecurityPreferences(application.applicationContext)

    private val _postSave = MutableLiveData<ValidationModel>()
    val postSave: LiveData<ValidationModel> = _postSave

    private val _communityList = MutableLiveData<List<CommunityModel>>()
    val communityList: LiveData<List<CommunityModel>> = _communityList

    fun save(post: PostModel) {
        if(post.post.isEmpty()) {

            _postSave.value = ValidationModel("Campo descrição obrigatório")
        } else {
            post.userId = securityPreferences.get(GameLinkConstants.SHARED.USER_ID).toInt()

            postRepository.save(post)
            _postSave.value = ValidationModel()
        }
    }

    fun loadCommunities() {
        val userId = securityPreferences.get(GameLinkConstants.SHARED.USER_ID).toInt()
        _communityList.value = communityRepository.getFollowed(userId)
    }
}