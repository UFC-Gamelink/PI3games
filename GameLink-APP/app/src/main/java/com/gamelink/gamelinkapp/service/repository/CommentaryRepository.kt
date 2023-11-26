package com.gamelink.gamelinkapp.service.repository

import android.content.Context
import com.gamelink.gamelinkapp.service.listener.APIListener
import com.gamelink.gamelinkapp.service.model.CommentaryAndProfileModel
import com.gamelink.gamelinkapp.service.model.CommentaryModel
import com.gamelink.gamelinkapp.service.repository.local.LocalDatabase
import com.gamelink.gamelinkapp.service.repository.remote.CommentaryDatabase

class CommentaryRepository(context: Context) {
    private val commentaryDatabase = CommentaryDatabase()

    suspend fun create(commentary: CommentaryModel, listener: APIListener<Boolean>) {
        try {
            commentaryDatabase.save(commentary.postId, commentary)

            listener.onSuccess(true)
        } catch (e: Exception) {
            listener.onFailure(e.message.toString())
        }

    }

    suspend fun listByPost(postId: String): List<CommentaryModel> {
        
            return commentaryDatabase.get(postId)


    }

    fun listWithProfileByPost(postId: Int): List<CommentaryModel> {
        return listOf()
    }

    fun delete(commentaryId: String) {

    }


}