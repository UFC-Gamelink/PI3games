package com.gamelink.gamelinkapp.service.repository

import android.content.Context
import com.gamelink.gamelinkapp.service.model.UserCommunityModel
import com.gamelink.gamelinkapp.service.repository.local.LocalDatabase

class UserCommunityRepository(context: Context) {

    fun joinCommunity(userCommunity: UserCommunityModel) {
        //database.joinCommunity(userCommunity)
    }

    fun leaveCommunity(userId: Int, communityId: Int) {
        //database.leaveCommunity(userId, communityId)
    }

    fun userIsJoin(userId: Int, communityId: Int):Boolean {
        return false
    }

    fun deleteMembers(id: String) {

    }
}