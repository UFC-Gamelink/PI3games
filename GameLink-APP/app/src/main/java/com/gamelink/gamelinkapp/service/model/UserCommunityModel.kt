package com.gamelink.gamelinkapp.service.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "user_community", primaryKeys = ["user_id", "community_id"])
class UserCommunityModel {
    @ColumnInfo(name = "user_id")
    var userId: Int = 0

    @ColumnInfo(name = "community_id")
    var communityId: Int = 0
}