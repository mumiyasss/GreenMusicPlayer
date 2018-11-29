package com.grebnevstudio.musicplayer.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songsToPlay")
data class Song(
    @PrimaryKey
    val path: String,

    val name: String
) {
    override fun toString(): String {
        return name
    }
}