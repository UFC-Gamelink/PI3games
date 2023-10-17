package com.gamelink.gamelinkapp.service.repository

import android.content.Context
import com.gamelink.gamelinkapp.service.model.PostModel
import com.gamelink.gamelinkapp.service.repository.local.LocalDatabase

class PostRepository(context: Context) {
    private val database = LocalDatabase.getDatabase(context).postDAO()
    fun save(post: PostModel) {
        database.save(post)
    }

    fun list(): List<PostModel> {
        return database.list()
    }

    fun listByUser(userId: Int):List<PostModel> {
        return database.listByUser(userId)
    }
}