package com.gamelink.gamelinkapp.service.repository

import android.content.Context
import com.gamelink.gamelinkapp.service.model.CommunityModel
import com.gamelink.gamelinkapp.service.repository.local.LocalDatabase

class CommunityRepository(context: Context) {
    private val database = LocalDatabase.getDatabase(context).communityDAO()
    fun save(community: CommunityModel) {
        database.save(community)
    }
}