package com.grebnevstudio.musicplayer.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.grebnevstudio.musicplayer.MusicPlayerApp
import com.grebnevstudio.musicplayer.db.Song
import com.grebnevstudio.musicplayer.db.SongsDao
import com.grebnevstudio.musicplayer.helpers.asyncOnMainThread
import com.grebnevstudio.musicplayer.service.PlayerServiceConnection
import javax.inject.Inject

class PlayerViewModel : ViewModel() {
    @Inject
    lateinit var songsDao: SongsDao
    @Inject
    lateinit var app: Application
    @Inject
    lateinit var serviceConnection: PlayerServiceConnection

    val isPlaying = MutableLiveData<Boolean>()

    fun uploadNewFolder(treeUri: Uri) {
        DocumentFile.fromTreeUri(app as Context, treeUri)?.let {
            it.listFiles().forEach { file ->
                when {
                    file.isDirectory -> { }
                    file.type == "audio/mpeg" -> {
                        songsDao.insert(
                            Song(
                                path = file.uri.toString(),
                                name = file.name ?: "UNKNOWN"
                            )
                        )
                    }
                }
            }
        }
    }

    init {
        MusicPlayerApp.component.injectPlayerViewModel(this)
        updateIsPlayingStatus()
    }

    private fun updateIsPlayingStatus() {
        asyncOnMainThread{
            serviceConnection.initServiceIfNeeded()
            isPlaying.value = serviceConnection.playerBinder.isPlaying()
        }
    }
}
