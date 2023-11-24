package com.gamelink.gamelinkapp.service.repository.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gamelink.gamelinkapp.service.model.UserCommunityModel

//@Dao
interface UserCommunityDAO {
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun joinCommunity(userCommunityFollowModel: UserCommunityModel)

//    @Query("DELETE FROM user_community WHERE user_id = :userId AND community_id = :communityId")
    fun leaveCommunity(userId: Int, communityId: Int)

//    @Query("SELECT * FROM user_community WHERE user_id = :userId AND community_id = :communityId")
    fun isJoin(userId: Int, communityId: Int): UserCommunityModel?

//    @Query("DELETE FROM user_community WHERE community_id = :communityId")
    fun deleteMembers(communityId: Int)
}