package com.grebnevstudio.musicplayer.helpers

import android.content.Context

open class BaseConfig(val context: Context){
    private val prefs = context.getSharedPrefs()

    var appTheme: Int
        get() = prefs.getInt(APP_THEME, DEFAULT_THEME)
        set(appTheme) = prefs.edit().putInt(APP_THEME, appTheme).apply()
}