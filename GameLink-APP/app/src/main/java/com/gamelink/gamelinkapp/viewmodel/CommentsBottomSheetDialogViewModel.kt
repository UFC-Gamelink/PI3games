package com.gamelink.gamelinkapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gamelink.gamelinkapp.service.constants.GameLinkConstants
import com.gamelink.gamelinkapp.service.listener.APIListener
import com.gamelink.gamelinkapp.service.model.CommentaryAndProfileModel
import com.gamelink.gamelinkapp.service.model.CommentaryModel
import com.gamelink.gamelinkapp.service.model.ValidationModel
import com.gamelink.gamelinkapp.service.repository.CommentaryRepository
import com.gamelink.gamelinkapp.service.repository.ProfileRepository
import com.gamelink.gamelinkapp.service.repository.SecurityPreferences
import kotlinx.coroutines.launch

class CommentsBottomSheetDialogViewModel(application: Application) : AndroidViewModel(application) {
    private val commentaryRepository = CommentaryRepository(application.applicationContext)
    private val profileRepository = ProfileRepository(application.applicationContext)
    private val securityPreferences = SecurityPreferences(application.applicationContext)

    private val _comments = MutableLiveData<List<CommentaryModel>>()
    val comments: LiveData<List<CommentaryModel>> = _comments

    private val _commentarySave = MutableLiveData<Boolean>()
    val commentarySave: LiveData<Boolean> = _commentarySave

    private val _profilePic = MutableLiveData<String>()
    val profilePic: LiveData<String> = _profilePic

    private val _deleteCommentary = MutableLiveData<ValidationModel>()
    val deleteCommentary: LiveData<ValidationModel> = _deleteCommentary

    fun listByPost(postId: String) {
        viewModelScope.launch {
            _comments.value = commentaryRepository.listByPost(postId)
        }

    }

    fun save(commentary: CommentaryModel) {
        viewModelScope.launch {
            commentary.apply {
                this.ownerId = securityPreferences.get(GameLinkConstants.SHARED.USER_ID)
            }

            commentaryRepository.create(commentary, object : APIListener<Boolean> {
                override fun onSuccess(result: Boolean) {
                    _commentarySave.value = true
                }

                override fun onFailure(message: String) {
                    _commentarySave.value = false
                }

            })

            _commentarySave.value = true
        }

    }

    fun delete(commentaryId: String) {
        viewModelScope.launch {
            commentaryRepository.delete(commentaryId, object : APIListener<Boolean> {
                override fun onSuccess(result: Boolean) {
                    _deleteCommentary.value = ValidationModel()
                }

                override fun onFailure(message: String) {
                    _deleteCommentary.value = ValidationModel(message)
                }

            })


        }

    }
}