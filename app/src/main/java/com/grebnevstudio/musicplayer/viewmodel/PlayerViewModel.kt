package com.grebnevstudio.musicplayer.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.grebnevstudio.musicplayer.MusicPlayerApp
import com.grebnevstudio.musicplayer.db.Song
import com.grebnevstudio.musicplayer.helpers.asyncOnBackgroundThread
import com.grebnevstudio.musicplayer.helpers.asyncOnMainThread
import com.grebnevstudio.musicplayer.service.PlayerServiceConnection
import kotlinx.coroutines.delay
import javax.inject.Inject

class PlayerViewModel : ViewModel() {
    @Inject
    lateinit var app: Application
    @Inject
    lateinit var serviceConnection: PlayerServiceConnection

    val currentPosition = MutableLiveData<Int>().apply {
        value = 0
    }

    private fun startUpdatingCurrentPosition() {
        asyncOnBackgroundThread {
            delay(1000)
            currentPosition.postValue(serviceConnection.getPlayer().getCurrentPosition())
            startUpdatingCurrentPosition()
        }
    }

    fun seekTo(seconds: Int) {
        asyncOnMainThread {
            serviceConnection.getPlayer().seekTo(seconds)
        }
    }

    suspend fun getActiveSong(): LiveData<Song> {
        return serviceConnection.getPlayer().getActiveSong()
    }

    fun playOrPauseSong() {
        asyncOnMainThread {
            serviceConnection.getPlayer().playOrPauseSong()
        }
    }

    fun playNext() {
        asyncOnMainThread {
            serviceConnection.getPlayer().next()
        }
    }

    fun playPrevious() {
        asyncOnMainThread {
            serviceConnection.getPlayer().previous()
        }
    }

    suspend fun playingStatus(): LiveData<Boolean> {
        return serviceConnection.getPlayer().isPlaying()
    }

    init {
        MusicPlayerApp.component.injectPlayerViewModel(this)
        startUpdatingCurrentPosition()
    }
}
