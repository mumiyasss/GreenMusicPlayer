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
import com.grebnevstudio.musicplayer.helpers.AUDIO_MIME
import com.grebnevstudio.musicplayer.helpers.CHOOSE_TRACK_STUB
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

    fun getSongs() = songsDao.getAll()

    val isPlaying = MutableLiveData<Boolean>()
    val activeSongTitle = MutableLiveData<String>().apply {
        value = CHOOSE_TRACK_STUB
    }

    fun uploadNewFolder(treeUri: Uri) {
        DocumentFile.fromTreeUri(app as Context, treeUri)?.let {
            it.listFiles().forEach { file ->
                when {
                    file.isDirectory -> {
                    }
                    file.type == AUDIO_MIME -> {
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

    private fun updateIsPlayingStatus() {
        asyncOnMainThread {
            serviceConnection.initServiceIfNeeded()
            // Todo: Точно ли нельзя переделать на LiveData
            isPlaying.value = serviceConnection.playerBinder.isPlaying()
            serviceConnection.playerBinder.getActiveSong()?.let { song ->
                activeSongTitle.value = song.name
            }
        }
    }

    fun clearPlaylist() {
        songsDao.deleteAll()
        activeSongTitle.value = CHOOSE_TRACK_STUB
    }

    fun playOrPauseSong(song: Song? = null) {
        asyncOnMainThread {
            serviceConnection.initServiceIfNeeded()
            serviceConnection.playerBinder.playOrPauseSong(song)
            updateIsPlayingStatus()
            song?.let {
                activeSongTitle.value = song.name
            }
        }
    }

    fun onSongsSetChanged(songs: List<Song>) {
        asyncOnMainThread {
            serviceConnection.initServiceIfNeeded()
            serviceConnection.playerBinder.setSongsList(songs)
        }
    }

    fun playNext() {
        asyncOnMainThread {
            serviceConnection.initServiceIfNeeded()
            serviceConnection.playerBinder.next()
            updateIsPlayingStatus()
        }
    }

    fun playPrevious() {
        asyncOnMainThread {
            serviceConnection.initServiceIfNeeded()
            serviceConnection.playerBinder.previous()
            updateIsPlayingStatus()
        }
    }

    init {
        MusicPlayerApp.component.injectPlayerViewModel(this)
        updateIsPlayingStatus()
    }
}
