package com.gamelink.gamelinkapp.service.repository.remote

import android.util.Log
import com.gamelink.gamelinkapp.service.model.ProfileModel
import com.gamelink.gamelinkapp.service.repository.remote.service.ProfileService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProfileDatabase {
    private val remote = RetrofitClient.getService(ProfileService::class.java)

    suspend fun save(profile: ProfileModel): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = remote.save(profile)

                if(response.code() != 200) {
                    throw Exception(response.errorBody()!!.string())
                }
                return@withContext true
            } catch(error: Exception) {
                Log.d("ProfileDatabase", error.message.toString())
                throw Exception(error.message.toString())
            }
        }
    }

    suspend fun get(): ProfileModel? {
        return withContext(Dispatchers.IO) {
            try {
                val profile = remote.get()

                return@withContext profile.body()
            } catch(error: Exception) {
                Log.d("ProfileDatabase", error.message.toString())
                throw Exception(error.message.toString())
            }
        }
    }
}