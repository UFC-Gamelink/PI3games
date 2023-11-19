package com.gamelink.gamelinkapp.service.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "commentary")
class CommentaryModel {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "comment_id")
    var id: Int = 0

    @ColumnInfo(name = "text")
    var text: String = ""

    @ColumnInfo(name = "post_id")
    var postId: Int = 0

    @ColumnInfo(name = "user_id")
    var userId: Int = 0
}