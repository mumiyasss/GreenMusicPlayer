package com.grebnevstudio.musicplayer

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.IBinder
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.grebnevstudio.musicplayer.helpers.ACTION_STOP_FOREGROUND_SERVICE
import com.grebnevstudio.musicplayer.helpers.showToast
import com.grebnevstudio.musicplayer.service.PlayerService
import androidx.core.app.ActivityCompat.startActivityForResult



class PlayerViewModel(application: Application) : AndroidViewModel(application) {
    val isPlaying = MutableLiveData<Boolean>()

    private val app = getApplication<Application>()
    private val serviceIntent = Intent(app, PlayerService::class.java)
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
        app.bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
        isPlaying.value = false
    }
}
