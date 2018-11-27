package com.grebnevstudio.musicplayer.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.grebnevstudio.musicplayer.R
import com.grebnevstudio.musicplayer.db.Song
import kotlinx.android.synthetic.main.song_list_item.view.*

class SongsAdapter(
    private val hostActivity: Context
) : RecyclerView.Adapter<SongViewHolder>() {
    var songs: List<Song> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        LayoutInflater.from(hostActivity).let { inflater ->
            val itemView = inflater.inflate(R.layout.song_list_item, parent, false)
            return SongViewHolder(itemView)
        }
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) =
        holder.bind(songs[position])

    override fun getItemCount() = songs.size
}

class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private var songTitle = itemView.song_title
    lateinit var song: Song

    fun bind(songToBind: Song) {
        song = songToBind
        songTitle.text = song.name
    }

    override fun onClick(v: View) {
    }

    init {
        itemView.setOnClickListener(this)
    }
}
