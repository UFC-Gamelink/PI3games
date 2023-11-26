package com.gamelink.gamelinkapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gamelink.gamelinkapp.service.constants.GameLinkConstants
import com.gamelink.gamelinkapp.service.listener.APIListener
import com.gamelink.gamelinkapp.service.model.PostModel
import com.gamelink.gamelinkapp.service.model.ValidationModel
import com.gamelink.gamelinkapp.service.repository.PostRepository
import com.gamelink.gamelinkapp.service.repository.SecurityPreferences
import kotlinx.coroutines.launch

class PostsUserViewModel(application: Application) : AndroidViewModel(application) {
    private val postsRepository = PostRepository(application.applicationContext)
    private val securityPreferences = SecurityPreferences(application.applicationContext)

    private val _posts = MutableLiveData<List<PostModel>>()
    val posts: LiveData<List<PostModel>> = _posts

    private val _delete = MutableLiveData<ValidationModel>()
    val delete: LiveData<ValidationModel> = _delete

    fun list() {
        viewModelScope.launch {
            _posts.value = postsRepository.list()
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
}