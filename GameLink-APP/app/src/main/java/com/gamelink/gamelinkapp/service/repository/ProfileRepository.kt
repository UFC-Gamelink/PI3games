package com.gamelink.gamelinkapp.service.repository

import android.content.Context
import com.gamelink.gamelinkapp.service.model.ProfileModel
import com.gamelink.gamelinkapp.service.repository.local.LocalDatabase

class ProfileRepository(context: Context) {
    private val database = LocalDatabase.getDatabase(context).profileDAO()

    fun save(profile: ProfileModel) {
        database.save(profile)
    }

    fun getByUser(userId: Int): ProfileModel? {
        return database.getByUserId(userId)
    }

    fun update(profile: ProfileModel) {
        database.update(profile)
    }
}