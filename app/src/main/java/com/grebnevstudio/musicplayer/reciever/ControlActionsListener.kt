package com.grebnevstudio.musicplayer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.grebnevstudio.musicplayer.helpers.*

class ControlActionsListener : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            ACTION_PLAY_PAUSE, ACTION_NEXT -> {
                context.sendIntent(intent.action ?: throw BroadcastActionNotFound())
            }
        }
    }
}