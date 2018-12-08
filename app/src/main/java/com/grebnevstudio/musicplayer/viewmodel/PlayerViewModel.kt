package com.grebnevstudio.musicplayer.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.grebnevstudio.musicplayer.MusicPlayerApp
import com.grebnevstudio.musicplayer.db.Song
import com.grebnevstudio.musicplayer.db.SongsDao
import com.grebnevstudio.musicplayer.helpers.AUDIO_MIME
import com.grebnevstudio.musicplayer.helpers.CHOOSE_TRACK_STUB
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
                                name = getTrackName(file.name)
                            )
                        )
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
        activeSongTitle.value = CHOOSE_TRACK_STUB
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
            updateIsPlayingStatus()
            song?.let {
                activeSongTitle.value = song.name
            }
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

    private fun updateIsPlayingStatus() {
        asyncOnMainThread {
            serviceConnection.initServiceIfNeeded()
            isPlaying.value = serviceConnection.playerBinder.isPlaying()
        }
    }

    init {
        MusicPlayerApp.component.injectPlayerViewModel(this)
        updateIsPlayingStatus()
    }
}
