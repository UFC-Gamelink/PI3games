package com.gamelink.gamelinkapp.service.repository.remote

import android.util.Log
import com.gamelink.gamelinkapp.service.model.CommentaryModel
import com.gamelink.gamelinkapp.service.model.CommunityModel
import com.gamelink.gamelinkapp.service.model.PostModel
import com.gamelink.gamelinkapp.service.repository.remote.service.CommunityService
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

class CommunityDatabase {
    private val remote = RetrofitClient.getService(CommunityService::class.java)

    suspend fun save(community: CommunityModel): CommunityModel {
        return withContext(Dispatchers.IO) {
            try {
                val response = remote.save(community)

                return@withContext response.body()!!
            } catch (ex: CancellationException) {
                throw ex
            } catch (error: Exception) {
                error.printStackTrace()
                Log.d("CommunityDatabase save", error.message.toString())
                throw Exception(error.message.toString())
            }
        }
    }

    suspend fun saveBanner(communityId: String, banner: MultipartBody.Part): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = remote.saveImage(banner, communityId)

                if (response.code() != 201) {
                    throw Exception(response.errorBody().toString())
                }

                return@withContext true
            } catch (ex: CancellationException) {
                throw ex
            } catch (error: Exception) {
                error.printStackTrace()
                Log.d("CommunityDatabase saveBanner", error.message.toString())
                throw Exception(error.message.toString())
            }
        }
    }

    suspend fun listAll(): List<CommunityModel> {
        return withContext(Dispatchers.IO) {
            try {
                val response = remote.getAll()

                return@withContext response.body()!!
            } catch (ex: CancellationException) {
                throw ex
            } catch (error: Exception) {
                error.printStackTrace()
                Log.d("CommunityDatabase listAll", error.message.toString())
                throw Exception(error.message.toString())
            }
        }
    }

    suspend fun get(id: String): CommunityModel {
        try {
            val response = remote.get(id)

            if (response.code() != 200) {
                throw Exception(response.errorBody().toString())
            }

            return response.body()!!
        } catch (ex: CancellationException) {
            throw ex
        } catch (error: Exception) {
            error.printStackTrace()
            Log.d("CommunityDatabase get", error.message.toString())
            throw Exception(error.message.toString())
        }
    }

    suspend fun getPosts(communityId: String): List<PostModel> {
        return withContext(Dispatchers.IO) {
            try {
                val response = remote.getPostsFromCommunity(communityId)

                if (response.code() != 200) {
                    throw Exception(response.errorBody().toString())
                }

                return@withContext response.body()!!
            }catch (ex: CancellationException) {
                throw ex
            } catch (error: Exception) {
                error.printStackTrace()
                Log.d("CommunityDatabase getPosts", error.message.toString())
                throw Exception(error.message.toString())
            }

        }
    }

    suspend fun delete(id: String) {
        try {
            remote.delete(id)
        } catch (ex: CancellationException) {
            throw ex
        } catch (error: Exception) {
            error.printStackTrace()
            Log.d("CommunityDatabase delete", error.message.toString())
            throw Exception(error.message.toString())
        }
    }

    suspend fun getMyCommunities(): List<CommunityModel> {
        try {
            val response = remote.getMyCommunities()

            if (response.code() != 200) {
                throw Exception(response.errorBody().toString())
            }

            return response.body()!!
        } catch (ex: CancellationException) {
            throw ex
        } catch (error: Exception) {
            error.printStackTrace()
            Log.d("CommunityDatabase getMyCommunities", error.message.toString())
            throw Exception(error.message.toString())
        }
    }

    suspend fun joinCommunity(communityId: String) {
        try {
            remote.enter(communityId)
        } catch (ex: CancellationException) {
            throw ex
        } catch (error: Exception) {
            error.printStackTrace()
            Log.d("CommunityDatabase joinCommunity", error.message.toString())
            throw Exception(error.message.toString())
        }
    }

    suspend fun leaveCommunity(communityId: String) {
        try {
            remote.exit(communityId)
        } catch (ex: CancellationException) {
            throw ex
        } catch (error: Exception) {
            error.printStackTrace()
            Log.d("CommunityDatabase joinCommunity", error.message.toString())
            throw Exception(error.message.toString())
        }
    }

    suspend fun update(community: CommunityModel): CommunityModel {
        return withContext(Dispatchers.IO) {
            try {
                val response = remote.update(community.id, community)

                if (response.code() != 200) {
                    throw Exception(response.errorBody().toString())
                }

                return@withContext response.body()!!
            } catch (ex: CancellationException) {
                throw ex
            } catch (error: Exception) {
                error.printStackTrace()
                Log.d("CommunityDatabase save", error.message.toString())
                throw Exception(error.message.toString())
            }
        }
    }

    suspend fun updateBanner(communityId: String, banner: MultipartBody.Part): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = remote.updateImage(communityId, banner)

                if (response.code() != 200) {
                    throw Exception(response.errorBody().toString())
                }

                return@withContext true
            } catch (ex: CancellationException) {
                throw ex
            } catch (error: Exception) {
                error.printStackTrace()
                Log.d("CommunityDatabase saveBanner", error.message.toString())
                throw Exception(error.message.toString())
            }
        }
    }
}