package com.grebnevstudio.musicplayer.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.grebnevstudio.musicplayer.MusicPlayerApp
import com.grebnevstudio.musicplayer.db.Song
import com.grebnevstudio.musicplayer.helpers.asyncOnMainThread
import com.grebnevstudio.musicplayer.service.PlayerServiceConnection
import javax.inject.Inject

class PlayerViewModel : ViewModel() {
    @Inject
    lateinit var app: Application
    @Inject
    lateinit var serviceConnection: PlayerServiceConnection

    suspend fun getActiveSong(): LiveData<Song> {
        serviceConnection.initServiceIfNeeded()
        return serviceConnection.playerBinder.getActiveSong()
    }

    fun playOrPauseSong() {
        asyncOnMainThread {
            serviceConnection.initServiceIfNeeded()
            serviceConnection.playerBinder.playOrPauseSong()
        }
    }

    fun playNext() {
        asyncOnMainThread {
            serviceConnection.initServiceIfNeeded()
            serviceConnection.playerBinder.next()
        }
    }

    fun playPrevious() {
        asyncOnMainThread {
            serviceConnection.initServiceIfNeeded()
            serviceConnection.playerBinder.previous()
        }
    }

    suspend fun isPlayingStatus(): LiveData<Boolean> {
        serviceConnection.initServiceIfNeeded()
        return serviceConnection.playerBinder.isPlaying()
    }

    init {
        MusicPlayerApp.component.injectPlayerViewModel(this)
    }
}
