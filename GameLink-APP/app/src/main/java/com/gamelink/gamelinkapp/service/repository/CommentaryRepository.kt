package com.gamelink.gamelinkapp.service.repository

import android.content.Context
import com.gamelink.gamelinkapp.service.model.CommentaryAndProfileModel
import com.gamelink.gamelinkapp.service.model.CommentaryModel
import com.gamelink.gamelinkapp.service.repository.local.LocalDatabase

class CommentaryRepository(context: Context) {

    fun create(commentary: CommentaryModel) {
        //database.create(commentary)
    }

    fun listByPost(postId: Int): List<CommentaryModel> {
        return listOf()
    }

    fun listWithProfileByPost(postId: Int): List<CommentaryAndProfileModel> {
        return listOf()
    }

    fun delete(commentaryId: Int) {

    }


}