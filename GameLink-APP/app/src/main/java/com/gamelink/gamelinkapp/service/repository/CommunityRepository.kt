package com.gamelink.gamelinkapp.service.repository

import android.content.Context
import com.gamelink.gamelinkapp.service.model.CommunityModel
import com.gamelink.gamelinkapp.service.repository.local.LocalDatabase

class CommunityRepository(context: Context) {
    fun create(community: CommunityModel) {

    }

    fun update(community: CommunityModel) {

    }

    fun list(): List<CommunityModel> {
        return listOf()
    }

    fun getById(id: Int): CommunityModel? {
        return null
    }

    fun getFollowed(userId: Int): List<CommunityModel> {
        return listOf()
    }

    fun delete(id: Int) {

    }
}