package com.grebnevstudio.musicplayer.viewmodel

import android.app.Application
import android.database.Cursor
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import com.grebnevstudio.musicplayer.MusicPlayerApp
import com.grebnevstudio.musicplayer.db.Song
import com.grebnevstudio.musicplayer.db.SongsDao
import com.grebnevstudio.musicplayer.extensions.getFilenameFromPath
import com.grebnevstudio.musicplayer.extensions.getIntValue
import com.grebnevstudio.musicplayer.extensions.getStringValue
import com.grebnevstudio.musicplayer.extensions.showToast
import com.grebnevstudio.musicplayer.helpers.PERMISSION_READ_STORAGE
import com.grebnevstudio.musicplayer.helpers.asyncOnBackgroundThread
import com.grebnevstudio.musicplayer.interfaces.PermissionHandler
import javax.inject.Inject

class GlobalViewModel : ViewModel() {
    @Inject
    lateinit var songsDao: SongsDao
    @Inject
    lateinit var app: Application

    fun findNewMusic(handler: PermissionHandler) {
        asyncOnBackgroundThread {
            songsDao.insertAll(scanDeviceForMp3Files(handler))
        }
    }

    private fun scanDeviceForMp3Files(handler: PermissionHandler): List<Song> {
        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"
        val projection = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.YEAR
        )
        val sortOrder = MediaStore.Audio.AudioColumns.TITLE + " COLLATE LOCALIZED ASC"
        val mp3Files = ArrayList<Song>()

        var cursor: Cursor? = null
        try {
            val uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            cursor = app.contentResolver.query(uri, projection, selection, null, sortOrder)
            if (cursor?.moveToFirst() == true) {
                while (!cursor.isAfterLast) {
                    val title = cursor.getStringValue(MediaStore.Audio.Media.TITLE)
                    val artist = cursor.getStringValue(MediaStore.Audio.Media.ARTIST)
                    val path = cursor.getStringValue(MediaStore.Audio.Media.DATA)
                    val duration = cursor.getIntValue(MediaStore.Audio.Media.DURATION) / 1000
                    val album = cursor.getStringValue(MediaStore.Audio.Media.ALBUM)
                    val year = cursor.getIntValue(MediaStore.Audio.Media.YEAR)
                    val song = Song(
                        title = getSongTitle(title, path),
                        artist = artist,
                        path = path,
                        duration = duration,
                        album = album,
                        year = year
                    )
                    cursor.moveToNext()
                    mp3Files.add(song)
                }
            }
        } catch (e: SecurityException) {
            handler.handlePermission(PERMISSION_READ_STORAGE) { hasAccess ->
                if (hasAccess)
                    findNewMusic(handler)
                else
                    app.showToast("Grant Permission to Read Files")
            }
        } finally {
            cursor?.close()
        }
        return mp3Files
    }

    private fun getSongTitle(title: String, path: String) =
        if (title == MediaStore.UNKNOWN_STRING) path.getFilenameFromPath() else title

    init {
        MusicPlayerApp.component.injectGlobalViewModel(this)
    }
}