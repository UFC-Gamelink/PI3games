package com.gamelink.gamelinkapp.service.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
class PostModel {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "post")
    var post: String = ""

    @ColumnInfo(name = "post_image_path")
    var postImagePath: String? = ""

    @ColumnInfo(name = "user_id")
    var userId: Int = 0

    @ColumnInfo(name = "visibility")
    var visibility: Int = 0

    @ColumnInfo(name = "created_at")
    var createdAt: String = ""
}