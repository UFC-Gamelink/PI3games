package com.gamelink.gamelinkapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gamelink.gamelinkapp.service.listener.APIListener
import com.gamelink.gamelinkapp.service.model.PostModel
import com.gamelink.gamelinkapp.service.model.ValidationModel
import com.gamelink.gamelinkapp.service.repository.CommunityRepository
import com.gamelink.gamelinkapp.service.repository.PostRepository
import kotlinx.coroutines.launch

class CommunityPostsViewModel(application: Application) : AndroidViewModel(application) {
    private val communityRepository = CommunityRepository(application.applicationContext)
    private val postsRepository = PostRepository(application.applicationContext)

    private val _posts = MutableLiveData<List<PostModel>>()
    val posts: LiveData<List<PostModel>> = _posts

    private val _delete = MutableLiveData<ValidationModel>()
    val delete: LiveData<ValidationModel> = _delete

    fun list(communityId: String) {
        viewModelScope.launch {
            _posts.value = communityRepository.getPosts(communityId)
        }
    }

    fun delete(id: String) {
        viewModelScope.launch {
            postsRepository.delete(id, object : APIListener<Boolean> {
                override fun onSuccess(result: Boolean) {
                    _delete.value = ValidationModel()

                }

                override fun onFailure(message: String) {
                    _delete.value = ValidationModel(message)

                }
            })
        }
    }

    fun like(postId: String) {
        viewModelScope.launch {
            postsRepository.like(postId)
        }
    }
}