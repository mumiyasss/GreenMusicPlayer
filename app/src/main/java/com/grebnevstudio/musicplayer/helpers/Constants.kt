package com.grebnevstudio.musicplayer.helpers

import android.content.Intent
import android.os.Build
import com.grebnevstudio.musicplayer.R

const val ACTION_NEXT = "ACTION_NEXT"
const val ACTION_PREVIOUS = "ACTION_PREVIOUS"
const val ACTION_PLAY_PAUSE = "ACTION_PLAY_PAUSE"
const val PREFS_KEY = "Prefs"

// shared preferences
const val APP_THEME = "app_theme"
const val DEFAULT_THEME = R.style.AppThemeDefault

val openFileIntent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
    addCategory(Intent.CATEGORY_OPENABLE)
    type = "audio/*"
}

fun isOreoPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O



