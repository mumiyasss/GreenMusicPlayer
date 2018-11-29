package com.grebnevstudio.musicplayer.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SongsDao {
    @Query("SELECT * FROM songsToPlay")
    fun getAll(): LiveData<List<Song>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(song: Song)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(songs: List<Song>)

    @Delete
    fun deleteSong(song: Song)

    @Query("DELETE FROM songsToPlay")
    fun deleteAll()
}