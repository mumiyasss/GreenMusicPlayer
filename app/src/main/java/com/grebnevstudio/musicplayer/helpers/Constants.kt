package com.grebnevstudio.musicplayer.helpers

import android.content.Context
import android.content.Intent
import android.os.Build
import com.grebnevstudio.musicplayer.service.PlayerService



const val ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE"
const val ACTION_NEXT = "ACTION_NEXT"
const val ACTION_PREVIOUS = "ACTION_PREVIOUS"
const val ACTION_PLAY_PAUSE = "ACTION_PLAY_PAUSE"

val openFileIntent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
    addCategory(Intent.CATEGORY_OPENABLE)
    type = "audio/*"
}

fun isOreoPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O



