package com.grebnevstudio.musicplayer.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songsToPlay")
data class Song(
    @PrimaryKey
    val path: String,

    val title: String,

    val artist: String,

    val album: String,

    val duration: Int,

    val year: Int
) {
    override fun toString(): String {
        return title
    }
}