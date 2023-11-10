package com.gamelink.gamelinkapp.service.repository.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gamelink.gamelinkapp.service.model.CommunityModel
import com.gamelink.gamelinkapp.service.model.PostModel
import com.gamelink.gamelinkapp.service.model.ProfileModel
import com.gamelink.gamelinkapp.service.model.UserCommunityModel
import com.gamelink.gamelinkapp.service.model.UserModel
import com.gamelink.gamelinkapp.service.repository.local.dao.CommunityDAO
import com.gamelink.gamelinkapp.service.repository.local.dao.PostDAO
import com.gamelink.gamelinkapp.service.repository.local.dao.ProfileDAO
import com.gamelink.gamelinkapp.service.repository.local.dao.UserCommunityDAO
import com.gamelink.gamelinkapp.service.repository.local.dao.UserDAO


@Database(
    entities = [
        PostModel::class,
        UserModel::class,
        ProfileModel::class,
        CommunityModel::class,
        UserCommunityModel::class], version = 1
)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun postDAO(): PostDAO
    abstract fun userDAO(): UserDAO
    abstract fun profileDAO(): ProfileDAO
    abstract fun communityDAO(): CommunityDAO
    abstract fun userCommunityDAO(): UserCommunityDAO

    companion object {
        private lateinit var INSTANCE: LocalDatabase

        fun getDatabase(context: Context): LocalDatabase {
            if (!Companion::INSTANCE.isInitialized) {
                synchronized(LocalDatabase::class) {
                    INSTANCE =
                        Room.databaseBuilder(context, LocalDatabase::class.java, "gamelinkDB")
                            .allowMainThreadQueries()
                            .build()
                }
            }

            return INSTANCE
        }
    }
}