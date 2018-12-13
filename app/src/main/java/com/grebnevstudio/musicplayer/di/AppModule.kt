package com.grebnevstudio.musicplayer.di

import android.app.Application
import android.content.Intent
import androidx.room.Room
import com.grebnevstudio.musicplayer.db.SongsDao
import com.grebnevstudio.musicplayer.db.SongsDatabase
import com.grebnevstudio.musicplayer.helpers.DB_FILE_NAME
import com.grebnevstudio.musicplayer.service.PlayerService
import com.grebnevstudio.musicplayer.service.PlayerServiceConnection
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    fun provideServiceIntent(app: Application): Intent {
        return Intent(app, PlayerService::class.java)
    }

    @Singleton
    @Provides
    fun provideSongsDao(app: Application): SongsDao {
        val db = Room.databaseBuilder(app, SongsDatabase::class.java, DB_FILE_NAME)
            .build()
        return db.songsDao()
    }

    @Singleton
    @Provides
    fun provideServiceConnection(): PlayerServiceConnection {
        return PlayerServiceConnection()
    }
}