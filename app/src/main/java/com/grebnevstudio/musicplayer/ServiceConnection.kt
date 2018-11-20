package com.grebnevstudio.musicplayer

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.grebnevstudio.musicplayer.service.PlayerService
import javax.inject.Inject

class PlayerServiceConnection private constructor() : ServiceConnection {
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

    init {
        MusicPlayerApp.component.injectServiceConnection(this)
    }

    fun connectService() {
        app.bindService(serviceIntent, this, Context.BIND_AUTO_CREATE)
    }

    fun disconnectService() {
        app.unbindService(this)
    }

    companion object {
        private var thisServiceConnectionObj: PlayerServiceConnection? = null
        fun get(): PlayerServiceConnection {
            return thisServiceConnectionObj ?: synchronized(this) {
                val tempConn = PlayerServiceConnection()
                thisServiceConnectionObj = tempConn
                return tempConn
            }
        }
    }
}

