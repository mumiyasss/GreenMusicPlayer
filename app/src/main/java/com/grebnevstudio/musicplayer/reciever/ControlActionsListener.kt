package com.grebnevstudio.musicplayer.reciever

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.grebnevstudio.musicplayer.helpers.ACTION_NEXT
import com.grebnevstudio.musicplayer.helpers.ACTION_PLAY_PAUSE
import com.grebnevstudio.musicplayer.helpers.BroadcastActionNotFound
import com.grebnevstudio.musicplayer.helpers.sendIntent

class ControlActionsListener : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            ACTION_PLAY_PAUSE, ACTION_NEXT -> {
                context.sendIntent(intent.action ?: throw BroadcastActionNotFound())
            }
        }
    }
}