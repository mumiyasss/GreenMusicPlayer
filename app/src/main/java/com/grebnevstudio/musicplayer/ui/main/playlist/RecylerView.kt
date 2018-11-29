package com.grebnevstudio.musicplayer.ui.main.playlist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.grebnevstudio.musicplayer.R
import com.grebnevstudio.musicplayer.db.Song
import com.grebnevstudio.musicplayer.viewmodel.PlayerViewModel
import kotlinx.android.synthetic.main.song_list_item.view.*

class SongsAdapter(
    private val hostActivity: Context,
    private val playerViewModel: PlayerViewModel
) : RecyclerView.Adapter<SongViewHolder>() {

    var songs: List<Song> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        LayoutInflater.from(hostActivity).let { inflater ->
            val itemView = inflater.inflate(R.layout.song_list_item, parent, false)
            return SongViewHolder(itemView, playerViewModel)
        }
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) =
        holder.bind(songs[position])

    override fun getItemCount() = songs.size
}

class SongViewHolder(
    itemView: View,
    private val playerViewModel: PlayerViewModel
) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private var songTitle = itemView.song_title
    private lateinit var song: Song

    fun bind(songToBind: Song) {
        song = songToBind
        songTitle.text = song.name
    }

    override fun onClick(itemView: View) {
        playerViewModel.playOrPauseSong(song)
    }

    init {
        itemView.setOnClickListener(this)
    }
}
