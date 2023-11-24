package com.gamelink.gamelinkapp.service.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//@Entity(tableName = "communities")
class CommunityModel {
//    @PrimaryKey(autoGenerate = true)
//    @ColumnInfo(name = "id")
    var id: Int = 0

//    @ColumnInfo(name = "name")
    var name: String = ""

//    @ColumnInfo(name = "description")
    var description: String = ""

//    @ColumnInfo(name = "banner_url")
    var bannerUrl: String? = ""

//    @ColumnInfo(name = "private")
    var private = false

//    @ColumnInfo(name = "owner_id")
    var ownerId: Int = 0

//    @ColumnInfo(name = "num_members")
    var numMembers: Int = 0
}