package com.grebnevstudio.musicplayer.viewmodel

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.IBinder
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.grebnevstudio.musicplayer.MusicPlayerApp
import com.grebnevstudio.musicplayer.helpers.ACTION_STOP_FOREGROUND_SERVICE
import com.grebnevstudio.musicplayer.helpers.showToast
import com.grebnevstudio.musicplayer.service.PlayerService
import javax.inject.Inject

class PlayerViewModel : ViewModel() {
    val isPlaying = MutableLiveData<Boolean>()

    @Inject
    lateinit var app: Application
    @Inject
    lateinit var serviceIntent: Intent
    private lateinit var mService: PlayerService
    private var isServiceBounded = false

    fun playPauseSong() {
        if (::mService.isInitialized) {
            mService.playOrPause()
            isPlaying.value = mService.isPlaying()
        } else
            app.showToast("Not so fast, wait a second please...")
    }

    fun stopService() {
        serviceIntent.action = ACTION_STOP_FOREGROUND_SERVICE
        app.startService(serviceIntent)
    }

    fun uploadFile(uri: Uri) {
        mService.uploadNewFile(uri)
        isPlaying.value = mService.isPlaying()
    }

    override fun onCleared() {
        super.onCleared()
        if (isServiceBounded) {
            app.unbindService(serviceConnection)
            isServiceBounded = false
        }
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(
            className: ComponentName,
            serviceIBinder: IBinder
        ) {
            val binder = serviceIBinder as PlayerService.PlayerServiceBinder
            mService = binder.service
            isServiceBounded = true
            isPlaying.value = mService.isPlaying()
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            isServiceBounded = false
            isPlaying.value = false
        }
    }

    init {
        MusicPlayerApp.component.injectViewModel(this)
        app.bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
        isPlaying.value = false
    }
}
