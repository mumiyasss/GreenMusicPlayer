package com.grebnevstudio.musicplayer.extensions

import android.content.Context
import android.widget.Toast
import com.grebnevstudio.musicplayer.helpers.BaseConfig
import com.grebnevstudio.musicplayer.helpers.Config
import com.grebnevstudio.musicplayer.helpers.PREFS_KEY

fun Context.showToast(message: String) =
    Toast.makeText(
        this, message,
        Toast.LENGTH_LONG
    ).show()

fun Context.getSharedPrefs() = getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)

val Context.baseConfig: BaseConfig
    get() = BaseConfig(
        this
    )

val Context.config: Config
    get() = Config(this)

