package com.gamelink.gamelinkapp.service.repository.local.dao

import androidx.room.Dao
import androidx.room.Insert
import com.gamelink.gamelinkapp.service.model.CommunityModel

@Dao
interface CommunityDAO {
    @Insert
    fun save(community: CommunityModel)
}