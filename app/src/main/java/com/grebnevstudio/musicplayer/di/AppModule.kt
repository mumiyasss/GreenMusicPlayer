package com.grebnevstudio.musicplayer.di

import android.app.Application
import android.content.Intent
import com.grebnevstudio.musicplayer.service.PlayerService
import dagger.Module
import dagger.Provides

@Module
class AppModule {
    @Provides
    fun provideServiceIntent(app: Application): Intent {
        return Intent(app, PlayerService::class.java)
    }
}