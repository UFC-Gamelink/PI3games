package com.gamelink.gamelinkapp.service.repository

import android.content.Context
import com.gamelink.gamelinkapp.service.listener.APIListener
import com.gamelink.gamelinkapp.service.model.CommunityModel
import com.gamelink.gamelinkapp.service.model.PostModel
import com.gamelink.gamelinkapp.service.model.ValidationModel
import com.gamelink.gamelinkapp.service.repository.local.LocalDatabase
import com.gamelink.gamelinkapp.service.repository.remote.CommunityDatabase
import okhttp3.MultipartBody

class CommunityRepository(context: Context) {
    private val communityDatabase = CommunityDatabase()
    suspend fun create(community: CommunityModel, listener: APIListener<CommunityModel>) {
        try {
            val communityId = communityDatabase.save(community)

            listener.onSuccess(communityId)

        } catch (e: Exception) {
            listener.onFailure(e.message.toString())
        }
    }

    suspend fun saveBanner(
        idCommunity: String,
        banner: MultipartBody.Part,
        listener: APIListener<Boolean>
    ) {
        try {
            communityDatabase.saveBanner(idCommunity, banner)

            listener.onSuccess(true)
        } catch (e: Exception) {
            listener.onFailure(e.message.toString())
        }
    }

    suspend fun update(community: CommunityModel, listener: APIListener<CommunityModel>) {
        try {
            val result = communityDatabase.update(community)

            listener.onSuccess(result)
        } catch (e: Exception) {
            listener.onFailure(e.message.toString())
        }
    }

    suspend fun updateBanner(
        idCommunity: String,
        banner: MultipartBody.Part,
        listener: APIListener<Boolean>
    ) {
        try {
            val result = communityDatabase.updateBanner(idCommunity, banner)

            listener.onSuccess(result)
        } catch (e: Exception) {
            listener.onFailure(e.message.toString())
        }
    }

    suspend fun list(): List<CommunityModel> {
        return communityDatabase.listAll()
    }

    suspend fun getPosts(communityId: String): List<PostModel> {
        return communityDatabase.getPosts(communityId)
    }

    suspend fun getById(id: String): CommunityModel? {
        return communityDatabase.get(id)
    }

    suspend fun getMyCommunities(): List<CommunityModel> {
        return communityDatabase.getMyCommunities()
    }


    suspend fun joinCommunity(communityId: String, listener: APIListener<Boolean>) {
        try {
            communityDatabase.joinCommunity(communityId)

            listener.onSuccess(true)
        } catch (e: Exception) {
            listener.onFailure(e.message.toString())
        }
    }

    suspend fun leaveCommunity(communityId: String, listener: APIListener<Boolean>) {
        try {
            communityDatabase.leaveCommunity(communityId)

            listener.onSuccess(true)
        } catch (e: Exception) {
            listener.onFailure(e.message.toString())
        }
    }


    suspend fun delete(id: String, listener: APIListener<Boolean>) {
        try {
            communityDatabase.delete(id)

            listener.onSuccess(true)
        } catch (e: Exception) {
            listener.onFailure(e.message.toString())
        }
    }
}