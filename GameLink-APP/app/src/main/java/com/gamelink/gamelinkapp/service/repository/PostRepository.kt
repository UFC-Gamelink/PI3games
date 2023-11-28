package com.gamelink.gamelinkapp.service.repository

import android.content.Context
import com.gamelink.gamelinkapp.service.listener.APIListener
import com.gamelink.gamelinkapp.service.mocks.MockPosts
import com.gamelink.gamelinkapp.service.model.PostModel
import com.gamelink.gamelinkapp.service.repository.remote.PostDatabase
import okhttp3.MultipartBody
import okhttp3.RequestBody

class PostRepository(context: Context) {
    private val postDatabase = PostDatabase()

    suspend fun save(
        description: RequestBody,
        image: MultipartBody.Part?,
        latitude: Double,
        longitude: Double,
        listener: APIListener<Boolean>,
    ) {
        try {
            postDatabase.save(description, image, latitude, longitude)

            listener.onSuccess(true)
        } catch (e: Exception) {
            listener.onFailure(e.message.toString())
        }
    }

    suspend fun saveForCommunity(
        communityId: String,
        description: RequestBody,
        image: MultipartBody.Part?,
        listener: APIListener<Boolean>
    ) {
        try {
            postDatabase.saveForCommunity(communityId, description, image)

            listener.onSuccess(true)
        } catch (e: Exception) {
            listener.onFailure(e.message.toString())
        }
    }

    suspend fun list(): List<PostModel> {
        return postDatabase.get()
    }

    fun listByUser(userId: Int): List<PostModel> {
        return listOf()
    }

    fun listByRecommended(): List<PostModel> {
        return MockPosts().getRecommendedPosts()
    }

    fun listByCommunity(communityId: Int): List<PostModel> {
        return listOf()
    }

    suspend fun delete(postId: String, listener: APIListener<Boolean>) {
        try {
            postDatabase.delete(postId)

            listener.onSuccess(true)
        } catch (e: Exception) {
            listener.onFailure(e.message.toString())
        }
    }

    suspend fun like(postID: String): Boolean {
        return postDatabase.like(postID)
    }

    fun deleteFromCommunity(id: String) {

    }

    fun findByIdAndUserId(id: String, userId: String): PostModel? {
        return null
    }
}