package com.grebnevstudio.musicplayer.helpers

import android.content.Context

class Config(context: Context) : BaseConfig(context) {
    var shuffleMode: Boolean
        get() = prefs.getBoolean(SHUFFLE_MODE, false)
        set(shuffle) = prefs.edit().putBoolean(SHUFFLE_MODE, shuffle).apply()

    var repeatMode: Boolean
        get() = prefs.getBoolean(REPEAT_MODE, false)
        set(repeat) = prefs.edit().putBoolean(REPEAT_MODE, repeat).apply()
}