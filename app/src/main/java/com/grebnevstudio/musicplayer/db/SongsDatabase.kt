package com.grebnevstudio.musicplayer.db

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.grebnevstudio.musicplayer.helpers.DB_FILE_NAME

@Database(entities = [Song::class], version = 2)
abstract class SongsDatabase : RoomDatabase() {
    abstract fun songsDao(): SongsDao

    companion object {
        private var db: SongsDatabase? = null
        fun getInstance(appContext: Application): SongsDatabase {
            if (db == null) {
                synchronized(SongsDatabase::class) {
                    db = Room.databaseBuilder(appContext, SongsDatabase::class.java, DB_FILE_NAME)
                        .build()
                }
            }
            return db!!
        }
    }
}
