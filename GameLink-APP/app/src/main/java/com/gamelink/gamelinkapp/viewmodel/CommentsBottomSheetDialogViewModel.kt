package com.gamelink.gamelinkapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gamelink.gamelinkapp.service.model.CommentaryModel
import com.gamelink.gamelinkapp.service.repository.CommentaryRepository

class CommentsBottomSheetDialogViewModel(application: Application) :
    AndroidViewModel(application) {
        private val commentaryRepository = CommentaryRepository(application.applicationContext)

        private val _comments = MutableLiveData<List<CommentaryModel>>()
        val comments: LiveData<List<CommentaryModel>> = _comments

        fun listByPost(postId: Int) {
            _comments.value = commentaryRepository.listByPost(postId)
        }
    }