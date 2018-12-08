package com.grebnevstudio.musicplayer.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.grebnevstudio.musicplayer.MusicPlayerApp
import com.grebnevstudio.musicplayer.db.Song
import com.grebnevstudio.musicplayer.db.SongsDao
import com.grebnevstudio.musicplayer.helpers.AUDIO_MIME
import com.grebnevstudio.musicplayer.helpers.asyncOnMainThread
import com.grebnevstudio.musicplayer.helpers.getTrackName
import com.grebnevstudio.musicplayer.service.PlayerServiceConnection
import java.io.Serializable
import javax.inject.Inject

// ToDo: diffrent PlayerViewModel instances for different fragments
class PlayerViewModel : ViewModel(), Serializable {
    @Inject
    lateinit var songsDao: SongsDao
    @Inject
    lateinit var app: Application
    @Inject
    lateinit var serviceConnection: PlayerServiceConnection

    fun getSongs() = songsDao.getAll()

    fun uploadNewFolder(treeUri: Uri) {
        asyncOnMainThread {
            DocumentFile.fromTreeUri(app as Context, treeUri)?.let {
                it.listFiles().forEach { file ->
                    when {
                        file.isDirectory -> {
                        }
                        file.type == AUDIO_MIME -> {
                            songsDao.insert(
                                Song(
                                    path = file.uri.toString(),
                                    name = getTrackName(file.name)
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    suspend fun getActiveSong(): LiveData<Song> {
        serviceConnection.initServiceIfNeeded()
        return serviceConnection.playerBinder.getActiveSong()
    }

    fun clearPlaylist() {
        songsDao.deleteAll()
    }

    fun onSongsSetChanged(songs: List<Song>) {
        asyncOnMainThread {
            serviceConnection.initServiceIfNeeded()
            serviceConnection.playerBinder.setSongsList(songs)
        }
    }

    fun playOrPauseSong(song: Song? = null) {
        asyncOnMainThread {
            serviceConnection.initServiceIfNeeded()
            serviceConnection.playerBinder.playOrPauseSong(song)
        }
    }

    fun playNext() {
        asyncOnMainThread {
            serviceConnection.initServiceIfNeeded()
            serviceConnection.playerBinder.next()
        }
    }

    fun playPrevious() {
        asyncOnMainThread {
            serviceConnection.initServiceIfNeeded()
            serviceConnection.playerBinder.previous()
        }
    }

    suspend fun isPlayingStatus(): LiveData<Boolean> {
        serviceConnection.initServiceIfNeeded()
        return serviceConnection.playerBinder.isPlaying()
    }

    init {
        MusicPlayerApp.component.injectPlayerViewModel(this)
    }
}
