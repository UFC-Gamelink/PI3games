package com.gamelink.gamelinkapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gamelink.gamelinkapp.service.constants.GameLinkConstants
import com.gamelink.gamelinkapp.service.model.CommentaryAndProfileModel
import com.gamelink.gamelinkapp.service.model.CommentaryModel
import com.gamelink.gamelinkapp.service.repository.CommentaryRepository
import com.gamelink.gamelinkapp.service.repository.SecurityPreferences

class CommentsBottomSheetDialogViewModel(application: Application) : AndroidViewModel(application) {
    private val commentaryRepository = CommentaryRepository(application.applicationContext)
    private val securityPreferences = SecurityPreferences(application.applicationContext)

    private val _comments = MutableLiveData<List<CommentaryAndProfileModel>>()
    val comments: LiveData<List<CommentaryAndProfileModel>> = _comments

    private val _commentarySave = MutableLiveData<Boolean>()
    val commentarySave: LiveData<Boolean> = _commentarySave

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
}