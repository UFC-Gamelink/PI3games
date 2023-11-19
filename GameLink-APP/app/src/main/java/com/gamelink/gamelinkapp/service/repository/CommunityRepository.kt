package com.gamelink.gamelinkapp.service.repository

import android.content.Context
import com.gamelink.gamelinkapp.service.model.CommunityModel
import com.gamelink.gamelinkapp.service.repository.local.LocalDatabase

class CommunityRepository(context: Context) {
    private val database = LocalDatabase.getDatabase(context).communityDAO()
    fun create(community: CommunityModel) {
        database.create(community)
    }

    fun update(community: CommunityModel) {
        database.update(community)
    }

    fun list(): List<CommunityModel> {
        return database.list()
    }

    fun getById(id: Int): CommunityModel? {
        return database.get(id)
    }

    fun getFollowed(userId: Int): List<CommunityModel> {
        return database.getCommunitiesIFollow(userId);
    }

    fun delete(id: Int) {
        database.delete(id)
    }
}