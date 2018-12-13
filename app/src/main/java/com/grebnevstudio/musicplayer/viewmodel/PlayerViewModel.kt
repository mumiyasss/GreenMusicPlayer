package com.grebnevstudio.musicplayer.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.grebnevstudio.musicplayer.MusicPlayerApp
import com.grebnevstudio.musicplayer.db.Song
import com.grebnevstudio.musicplayer.extensions.config
import com.grebnevstudio.musicplayer.helpers.asyncOnBackgroundThread
import com.grebnevstudio.musicplayer.helpers.asyncOnMainThread
import com.grebnevstudio.musicplayer.service.PlayerServiceConnection
import kotlinx.coroutines.delay
import javax.inject.Inject

class PlayerViewModel : ViewModel(), LifecycleObserver {
    @Inject
    lateinit var app: Application
    @Inject
    lateinit var serviceConnection: PlayerServiceConnection
    /**
     * Says, whether LifecycleOwner is active or not, to prevent doing
     * unnecessary things.
     */
    private var activeStatus = false

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun setActive() {
        activeStatus = true
        startUpdatingCurrentPosition()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun setNotActive() {
        activeStatus = false
    }

    val currentPosition = MutableLiveData<Int>().apply {
        value = 0
    }
    val shuffleMode = MutableLiveData<Boolean>()
    val repeatMode = MutableLiveData<Boolean>()

    private fun startUpdatingCurrentPosition() {
        if (activeStatus)
            asyncOnBackgroundThread {
                currentPosition.postValue(serviceConnection.getPlayer().getCurrentPosition())
                delay(1000)
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

    fun onShufflePressed() {
        app.config.shuffleMode = !app.config.shuffleMode
        shuffleMode.value = app.config.shuffleMode
    }

    fun onRepeatPressed() {
        app.config.repeatMode = !app.config.repeatMode
        repeatMode.value = app.config.repeatMode
    }

    override fun onCleared() {
        activeStatus = false
        super.onCleared()
    }

    init {
        MusicPlayerApp.component.injectPlayerViewModel(this)
        asyncOnBackgroundThread {
            shuffleMode.postValue(app.config.shuffleMode)
            repeatMode.postValue(app.config.repeatMode)
        }
    }
}
