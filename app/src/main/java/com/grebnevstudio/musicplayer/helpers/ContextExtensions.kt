package com.grebnevstudio.musicplayer.helpers

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.grebnevstudio.musicplayer.service.PlayerService

fun Context.sendIntent(action: String) {
    Intent(this, PlayerService::class.java).apply {
        this.action = action
        try {
            startService(this@apply)
        } catch (ignored: Exception) {
        }
    }
}

fun Context.showToast(message: String) =
    Toast.makeText(
        this, message,
        Toast.LENGTH_LONG
    ).show()

