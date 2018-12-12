package com.grebnevstudio.musicplayer.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Song::class], version = 2)
abstract class SongsDatabase : RoomDatabase() {
    abstract fun songsDao(): SongsDao
}
