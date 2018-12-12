package com.grebnevstudio.musicplayer.extensions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.grebnevstudio.musicplayer.helpers.BaseConfig
import com.grebnevstudio.musicplayer.helpers.Config
import com.grebnevstudio.musicplayer.helpers.PERMISSION_READ_STORAGE
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

fun Context.hasPermission(permId: Int) = ContextCompat.checkSelfPermission(this, getPermissionString(permId)) == PackageManager.PERMISSION_GRANTED

fun Context.getPermissionString(id: Int) = when (id) {
    PERMISSION_READ_STORAGE -> Manifest.permission.READ_EXTERNAL_STORAGE
    else -> ""
}
