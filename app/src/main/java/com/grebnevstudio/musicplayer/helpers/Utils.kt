package com.grebnevstudio.musicplayer.helpers

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun asyncOnMainThread(myFun: suspend CoroutineScope.() -> Unit) {
    GlobalScope.launch(Dispatchers.Main) {
        myFun()
    }
}

fun asyncOnBackgroundThread(myFun: suspend CoroutineScope.() -> Unit) {
    GlobalScope.launch(Dispatchers.IO) {
        myFun()
    }
}

fun getTrackName(fileName: String?) =
    fileName?.substring(0, fileName.lastIndexOf('.')) ?: UNKNOWN

///---  Only for Debug  ---///
fun logg(string: String) {
    Log.d("DDDD", string)
}