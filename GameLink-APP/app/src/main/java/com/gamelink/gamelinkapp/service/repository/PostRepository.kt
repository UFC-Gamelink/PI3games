package com.gamelink.gamelinkapp.service.repository

import android.content.Context
import com.gamelink.gamelinkapp.service.mocks.MockPosts
import com.gamelink.gamelinkapp.service.model.PostModel
import com.gamelink.gamelinkapp.service.model.PostProfileModel
import com.gamelink.gamelinkapp.service.repository.local.LocalDatabase

class PostRepository(context: Context) {

    fun save(post: PostModel) {

    }

    fun list(): List<PostProfileModel> {
        return listOf()
    }

    fun listByUser(userId: Int): List<PostProfileModel> {
        return listOf()
    }

    fun listByRecommended(): List<PostProfileModel> {
        val mockPosts = MockPosts()

        return listOf()
        //return mockPosts.getRecommendedPosts()
    }

    fun listByCommunity(communityId: Int): List<PostProfileModel> {
        return listOf()
    }

    fun delete(postId: Int) {

    }

    fun deleteFromCommunity(id: Int) {

    }

    fun findByIdAndUserId(id: Int, userId: Int): PostModel? {
        return null
    }
}