package com.gamelink.gamelinkapp.service.repository

import android.content.Context
import com.gamelink.gamelinkapp.service.listener.APIListener
import com.gamelink.gamelinkapp.service.model.ProfileModel
import com.gamelink.gamelinkapp.service.repository.local.LocalDatabase
import com.gamelink.gamelinkapp.service.repository.remote.ProfileDatabase
import okhttp3.MultipartBody

class ProfileRepository(context: Context) {
    private val profileDatabase = ProfileDatabase()

    suspend fun save(profile: ProfileModel, listener: APIListener<Boolean>) {
        try {
            profileDatabase.save(profile)

            listener.onSuccess(true)
        } catch (e: Exception) {
            listener.onFailure(e.message.toString())
        }

    }

    suspend fun saveImages(
        icon: MultipartBody.Part,
        banner: MultipartBody.Part,
        listener: APIListener<ProfileModel>
    ) {
        try {
            val profile = profileDatabase.saveImages(icon, banner)

            listener.onSuccess(profile)
        } catch (e: Exception) {
            listener.onFailure(e.message.toString())
        }
    }

    suspend fun getByUser(): ProfileModel? {
        return profileDatabase.get()
    }

    suspend fun update(profile: ProfileModel, listener: APIListener<Boolean>) {
        try {
            profileDatabase.update(profile)

            listener.onSuccess(true)
        }catch (e: Exception) {
            listener.onFailure(e.message.toString())
        }
    }

    fun getByUser(userId: Int): ProfileModel? {
        return null
    }
}