package com.grebnevstudio.musicplayer.helpers

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun asyncOnMainThread(myFun: suspend CoroutineScope.() -> Unit) {
    GlobalScope.launch(Dispatchers.Main) {
        myFun()
    }
}

fun getTrackName(fileName: String?) =
    fileName?.substring(0, fileName.lastIndexOf('.')) ?: UNKNOWN
