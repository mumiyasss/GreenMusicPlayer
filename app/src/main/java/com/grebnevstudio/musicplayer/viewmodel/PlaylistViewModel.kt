package com.grebnevstudio.musicplayer.viewmodel

import androidx.lifecycle.ViewModel
import com.grebnevstudio.musicplayer.MusicPlayerApp
import com.grebnevstudio.musicplayer.db.Song
import com.grebnevstudio.musicplayer.db.SongsDao
import com.grebnevstudio.musicplayer.helpers.asyncOnMainThread
import com.grebnevstudio.musicplayer.service.PlayerServiceConnection
import javax.inject.Inject

class PlaylistViewModel : ViewModel() {
    @Inject
    lateinit var songsDao: SongsDao
    @Inject
    lateinit var serviceConnection: PlayerServiceConnection

    fun getSongs() = songsDao.getAll()

    fun clearPlaylist() {
        songsDao.deleteAll()
    }

    fun onSongsSetChanged(songs: List<Song>) {
        asyncOnMainThread {
            serviceConnection.getPlayer().setSongsList(songs)
        }
    }

    fun playOrPauseSong(song: Song) {
        asyncOnMainThread {
            serviceConnection.getPlayer().playOrPauseSong(song)
        }
    }

    init {
        MusicPlayerApp.component.injectPlaylistViewModel(this)
    }

//    fun uploadNewFolder(treeUri: Uri) {
//        asyncOnMainThread {
//            DocumentFile.fromTreeUri(app as Context, treeUri)?.let {
//                it.listFiles().forEach { file ->
//                    when {
//                        file.isDirectory -> {
//                        }
//                        file.type == AUDIO_MIME -> {
//                            songsDao.insert(
//                                Song(
//                                    path = file.uri.toString(),
//                                    title = getTrackName(file.name)
//                                )
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }
}