package com.gamelink.gamelinkapp.service.repository.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.gamelink.gamelinkapp.service.model.UserAndProfileModel
import com.gamelink.gamelinkapp.service.model.UserModel

//@Dao
interface UserDAO {
//    @Insert
    fun save(user: UserModel)

//    @Query("SELECT * FROM USERS WHERE username = :username")
    fun login(username: String): UserModel?

//    @Transaction
//    @Query("SELECT * FROM users where id = :userId")
    fun getUserAndProfile(userId: Int): UserAndProfileModel

//    @Update
    fun update(user: UserModel)
}