package com.grebnevstudio.musicplayer.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SongsDao {
    @Query("SELECT * FROM songs")
    fun getAll(): LiveData<List<Song>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(song: Song)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(songs: List<Song>)

    @Delete
    fun deleteSong(song: Song)

    @Query("DELETE FROM songs")
    fun deleteAll()
}