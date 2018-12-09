package com.grebnevstudio.musicplayer.extensions


fun Int.toMinutesSecondsFormat() : String {
    val seconds = this % 60
    val minutes = this / 60
    return if (seconds / 10 == 0) "$minutes:0$seconds"
                             else "$minutes:$seconds"
}