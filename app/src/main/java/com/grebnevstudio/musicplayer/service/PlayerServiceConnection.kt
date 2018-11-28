package com.grebnevstudio.musicplayer.service

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.grebnevstudio.musicplayer.MusicPlayerApp
import kotlinx.coroutines.delay
import javax.inject.Inject

class PlayerServiceConnection : ServiceConnection {
    @Inject
    lateinit var app: Application
    @Inject
    lateinit var serviceIntent: Intent
    var bounded = false
    /**
     * Before calling implicit get() method, checking that
     * (bounded == true) is required.
     */
    lateinit var playerBinder: PlayerService.PlayerServiceBinder

    override fun onServiceConnected(className: ComponentName, serviceIBinder: IBinder) {
        playerBinder = serviceIBinder as PlayerService.PlayerServiceBinder
        bounded = true
    }

    override fun onServiceDisconnected(arg0: ComponentName) {
        bounded = false
    }

    fun disconnectService() {
        bounded = false
        playerBinder.stopService()
        app.unbindService(this)
    }

    suspend fun initServiceIfNeeded() {
        if (!bounded) {
            connectService()
            while (!bounded)
                delay(10)
        }
    }

    private fun connectService() {
        app.bindService(serviceIntent, this, Context.BIND_AUTO_CREATE)
    }

    init {
        MusicPlayerApp.component.injectServiceConnection(this)
    }
}

