package com.gamelink.gamelinkapp.service.repository.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.gamelink.gamelinkapp.service.model.ProfileModel

@Dao
interface ProfileDAO {
    @Insert
    fun save(profile: ProfileModel)

    @Query("SELECT profiles.*, count(posts.id) AS num_posts FROM profiles LEFT JOIN posts ON profiles.user_id = posts.user_id WHERE profiles.user_id = :userId GROUP BY profiles.user_id")
    fun getByUserId(userId: Int): ProfileModel?

    @Update
    fun update(profile: ProfileModel)

}