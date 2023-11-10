package com.gamelink.gamelinkapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gamelink.gamelinkapp.service.constants.GameLinkConstants
import com.gamelink.gamelinkapp.service.model.PostModel
import com.gamelink.gamelinkapp.service.repository.PostRepository

class CommunityPostsViewModel(application: Application) : AndroidViewModel(application) {
    private val postsRepository = PostRepository(application.applicationContext)

    private val _posts = MutableLiveData<List<PostModel>>()
    val posts: LiveData<List<PostModel>> = _posts

    fun list(communityId: Int) {
        _posts.value = postsRepository.listByCommunity(communityId)
    }
}