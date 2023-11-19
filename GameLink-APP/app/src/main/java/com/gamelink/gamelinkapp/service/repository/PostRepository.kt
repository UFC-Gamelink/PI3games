package com.gamelink.gamelinkapp.service.repository

import android.content.Context
import com.gamelink.gamelinkapp.service.mocks.MockPosts
import com.gamelink.gamelinkapp.service.model.PostModel
import com.gamelink.gamelinkapp.service.model.PostProfileModel
import com.gamelink.gamelinkapp.service.repository.local.LocalDatabase

class PostRepository(context: Context) {
    private val database = LocalDatabase.getDatabase(context).postDAO()
    fun save(post: PostModel) {
        database.save(post)
    }

    fun list(): List<PostProfileModel> {
        return database.list()
    }

    fun listByUser(userId: Int): List<PostProfileModel> {
        return database.listByUser(userId)
    }

    fun listByRecommended(): List<PostProfileModel> {
        val mockPosts = MockPosts()
        return mockPosts.getRecommendedPosts()
    }

    fun listByCommunity(communityId: Int): List<PostProfileModel> {
        return database.listByCommunity(communityId)
    }

    fun delete(postId: Int) {
        database.delete(postId)
    }

    fun findByIdAndUserId(id: Int, userId: Int): PostModel? {
        return database.findByIdAndUserId(id, userId)
    }
}