package com.grebnevstudio.musicplayer.extensions

fun String.getFilenameFromPath() = substring(lastIndexOf("/") + 1)
