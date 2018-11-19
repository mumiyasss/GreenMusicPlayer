package com.grebnevstudio.musicplayer

import android.app.Application
import com.grebnevstudio.musicplayer.di.AppComponent
import com.grebnevstudio.musicplayer.di.DaggerAppComponent

class MusicPlayerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent
            .builder()
            .application(this)
            .build()
    }

    companion object {
        lateinit var component: AppComponent
    }
}