package com.gamelink.gamelinkapp.service.repository.remote

import android.util.Log
import com.gamelink.gamelinkapp.service.model.ProfileModel
import com.gamelink.gamelinkapp.service.repository.remote.service.ProfileService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

class ProfileDatabase {
    private val remote = RetrofitClient.getService(ProfileService::class.java)

    suspend fun save(profile: ProfileModel): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = remote.save(profile)

                if(response.code() != 201) {
                    throw Exception(response.errorBody()!!.string())
                }
                return@withContext true
            } catch(error: Exception) {
                error.printStackTrace()
                Log.d("ProfileDatabase save", error.message.toString())
                throw Exception(error.message.toString())
            }
        }
    }

    suspend fun update(profile: ProfileModel): ProfileModel {
        return withContext(Dispatchers.IO) {
            try {
                val response = remote.update(profile)

                if(response.code() != 200) {
                    throw Exception(response.errorBody()!!.string())
                }

                return@withContext response.body()!!
            } catch(error: Exception) {
                error.printStackTrace()
                Log.d("ProfileDatabase update", error.message.toString())
                throw Exception(error.message.toString())
            }
        }
    }

    suspend fun saveImages(icon: MultipartBody.Part, banner: MultipartBody.Part): ProfileModel {
        return withContext(Dispatchers.IO) {
            try {
                val response = remote.saveImages(icon, banner)


                if(response.code() != 202) {
                    throw Exception(response.errorBody()!!.string())
                }

                return@withContext response.body()!!
            } catch (error:Exception) {
                error.printStackTrace()
                Log.d("ProfileDatabase saveImages", error.message.toString())
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
                Log.d("ProfileDatabase get", error.message.toString())
                throw Exception(error.message.toString())
            }
        }
    }
}