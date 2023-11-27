package com.gamelink.gamelinkapp.service.repository

import android.content.Context
import com.gamelink.gamelinkapp.service.listener.APIListener
import com.gamelink.gamelinkapp.service.model.CommunityModel
import com.gamelink.gamelinkapp.service.model.ValidationModel
import com.gamelink.gamelinkapp.service.repository.local.LocalDatabase
import com.gamelink.gamelinkapp.service.repository.remote.CommunityDatabase

class CommunityRepository(context: Context) {
    private val communityDatabase = CommunityDatabase()
    suspend fun create(community: CommunityModel, listener: APIListener<Boolean>) {
        try {
            communityDatabase.save(community)

            listener.onSuccess(true)

        } catch (e: Exception) {
            listener.onFailure(e.message.toString())
        }
    }

    fun update(community: CommunityModel) {

    }

    suspend fun list(): List<CommunityModel> {
        return communityDatabase.listAll()
    }

    fun getById(id: String): CommunityModel? {
        return null
    }

    fun getFollowed(userId: Int): List<CommunityModel> {
        return listOf()
    }

    fun delete(id: String) {

    }
}