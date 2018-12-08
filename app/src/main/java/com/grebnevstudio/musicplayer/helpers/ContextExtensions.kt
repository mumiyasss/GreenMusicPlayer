package com.grebnevstudio.musicplayer.helpers

import android.content.Context
import android.widget.Toast

fun Context.showToast(message: String) =
    Toast.makeText(
        this, message,
        Toast.LENGTH_LONG
    ).show()


fun Context.getSharedPrefs() = getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)

val Context.baseConfig: BaseConfig get() = BaseConfig(this)

