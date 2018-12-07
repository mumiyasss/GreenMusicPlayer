package com.grebnevstudio.musicplayer.helpers

import android.content.Intent
import android.os.Build
import com.grebnevstudio.musicplayer.R

const val ACTION_NEXT = "ACTION_NEXT"
const val ACTION_PREVIOUS = "ACTION_PREVIOUS"
const val ACTION_PLAY_PAUSE = "ACTION_PLAY_PAUSE"

// shared preferences
const val APP_THEME = "app_theme"
const val DEFAULT_THEME = R.style.AppThemeDefault

// application constants
const val PREFS_KEY = "Prefs"
const val DB_FILE_NAME = "songsToPlay.db"
const val AUDIO_MIME = "audio/mpeg"
const val CHOOSE_TRACK_STUB = "Выберите композицию"
const val FILE_NOT_FOUND = "File not found"
const val UNKNOWN = "UNKNOWN"

val openFileIntent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
    addCategory(Intent.CATEGORY_OPENABLE)
    type = "audio/*"
}

val openFolderIntent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)

fun isOreoPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
fun isLolipopPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
