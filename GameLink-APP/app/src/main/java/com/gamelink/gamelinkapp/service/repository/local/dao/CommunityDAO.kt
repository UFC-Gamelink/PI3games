package com.gamelink.gamelinkapp.service.repository.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.gamelink.gamelinkapp.service.model.CommunityModel

@Dao
interface CommunityDAO {
    @Insert
    fun save(community: CommunityModel)

    @Query("SELECT * FROM communities")
    fun list(): List<CommunityModel>

    @Query("SELECT * FROM communities WHERE id = :id")
    fun get(id: Int): CommunityModel?

    @Query("SELECT c.* " +
            "FROM communities c " +
            "WHERE c.owner_id = :userId " +
            "UNION " +
            "SELECT c.* " +
            "FROM communities c " +
            "INNER JOIN user_community u ON c.id = u.community_id " +
            "WHERE u.user_id = :userId")
    fun getCommunitiesIFollow(userId: Int): List<CommunityModel>
}