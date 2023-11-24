package com.gamelink.gamelinkapp.service.repository

import android.content.Context
import com.gamelink.gamelinkapp.service.listener.APIListener
import com.gamelink.gamelinkapp.service.model.ProfileModel
import com.gamelink.gamelinkapp.service.repository.local.LocalDatabase
import com.gamelink.gamelinkapp.service.repository.remote.ProfileDatabase

class ProfileRepository(context: Context) {
    private val profileDatabase = ProfileDatabase()
    private val database = LocalDatabase.getDatabase(context).profileDAO()

    suspend fun save(profile: ProfileModel, listener: APIListener<Boolean>) {
        try {
            profileDatabase.save(profile)

            listener.onSuccess(true)
        } catch(e: Exception) {
            listener.onFailure(e.message.toString())
        }

    }

    fun save(profile: ProfileModel) {

    }

    suspend fun getByUser(): ProfileModel? {
        return profileDatabase.get()
    }

    fun update(profile: ProfileModel) {
        database.update(profile)
    }

    fun getByUser(userId: Int) : ProfileModel? {
        return null
    }
}