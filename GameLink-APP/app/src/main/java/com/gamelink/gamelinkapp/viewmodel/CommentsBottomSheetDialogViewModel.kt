package com.gamelink.gamelinkapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gamelink.gamelinkapp.service.constants.GameLinkConstants
import com.gamelink.gamelinkapp.service.model.CommentaryAndProfileModel
import com.gamelink.gamelinkapp.service.model.CommentaryModel
import com.gamelink.gamelinkapp.service.model.ValidationModel
import com.gamelink.gamelinkapp.service.repository.CommentaryRepository
import com.gamelink.gamelinkapp.service.repository.ProfileRepository
import com.gamelink.gamelinkapp.service.repository.SecurityPreferences

class CommentsBottomSheetDialogViewModel(application: Application) : AndroidViewModel(application) {
    private val commentaryRepository = CommentaryRepository(application.applicationContext)
    private val profileRepository = ProfileRepository(application.applicationContext)
    private val securityPreferences = SecurityPreferences(application.applicationContext)

    private val _comments = MutableLiveData<List<CommentaryAndProfileModel>>()
    val comments: LiveData<List<CommentaryAndProfileModel>> = _comments

    private val _commentarySave = MutableLiveData<Boolean>()
    val commentarySave: LiveData<Boolean> = _commentarySave

    private val _profilePic = MutableLiveData<String>()
    val profilePic: LiveData<String> = _profilePic

    private val _deleteCommentary = MutableLiveData<ValidationModel>()
    val deleteCommentary: LiveData<ValidationModel> = _deleteCommentary

    fun listByPost(postId: Int) {
        _comments.value = commentaryRepository.listWithProfileByPost(postId)
    }

    fun save(commentary: CommentaryModel) {
        commentary.apply {
            this.userId = securityPreferences.get(GameLinkConstants.SHARED.USER_ID).toInt()
        }

        commentaryRepository.create(commentary)

        _commentarySave.value = true
    }

    fun getProfile() {
        val userId = securityPreferences.get(GameLinkConstants.SHARED.USER_ID).toInt()

        val profile = profileRepository.getByUser(userId)

        _profilePic.value = profile?.profilePicPath
    }

    fun delete(commentaryId: Int) {
        commentaryRepository.delete(commentaryId)

        _deleteCommentary.value = ValidationModel()
    }
}