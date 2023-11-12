package com.gamelink.gamelinkapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gamelink.gamelinkapp.service.constants.GameLinkConstants
import com.gamelink.gamelinkapp.service.model.PostProfileModel
import com.gamelink.gamelinkapp.service.model.ValidationModel
import com.gamelink.gamelinkapp.service.repository.PostRepository
import com.gamelink.gamelinkapp.service.repository.SecurityPreferences

class CommunitiesPostsViewModel(application: Application) : AndroidViewModel(application)  {
    private val postsRepository = PostRepository(application.applicationContext)
    private val securityPreferences = SecurityPreferences(application.applicationContext)

    private val _posts = MutableLiveData<List<PostProfileModel>>()
    val posts: LiveData<List<PostProfileModel>> = _posts

    private val _delete = MutableLiveData<ValidationModel>()
    val delete: LiveData<ValidationModel> = _delete

    fun list(communityId: Int) {

        _posts.value = postsRepository.listByCommunity(communityId)
    }

    fun delete(id: Int) {
        val userId = securityPreferences.get(GameLinkConstants.SHARED.USER_ID)

        val post = postsRepository.findByIdAndUserId(id, userId.toInt())

        if(post == null) {
            _delete.value = ValidationModel("Operação não autorizada")
        } else {
            postsRepository.delete(post.post.id)

            _delete.value = ValidationModel()
        }
    }
}