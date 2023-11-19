package com.gamelink.gamelinkapp.service.repository

import android.content.Context
import com.gamelink.gamelinkapp.service.model.CommentaryModel
import com.gamelink.gamelinkapp.service.repository.local.LocalDatabase

class CommentaryRepository(context: Context) {
    private val database = LocalDatabase.getDatabase(context).commentaryDAO()

    fun create(commentary: CommentaryModel) {
        database.create(commentary)
    }

    fun listByPost(postId: Int): List<CommentaryModel> {
        return database.listByPost(postId)
    }
}