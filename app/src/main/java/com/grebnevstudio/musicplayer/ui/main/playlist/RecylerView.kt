package com.grebnevstudio.musicplayer.ui.main.playlist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.grebnevstudio.musicplayer.R
import com.grebnevstudio.musicplayer.db.Song
import com.grebnevstudio.musicplayer.viewmodel.PlaylistViewModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_song_list.*

class SongsAdapter(
    private val hostActivity: Context,
    private val viewModel: PlaylistViewModel
) : RecyclerView.Adapter<SongViewHolder>() {

    var songs: List<Song> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        LayoutInflater.from(hostActivity).let { inflater ->
            val itemView = inflater.inflate(R.layout.item_song_list, parent, false)
            return SongViewHolder(itemView, viewModel)
        }
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) =
        holder.bind(songs[position])

    override fun getItemCount() = songs.size
}

/**
 * Layout container for more performance. (AndroidExtensions { experimental = true })
 */
class SongViewHolder(
    override val containerView: View,
    private val playlistViewModel: PlaylistViewModel
) : RecyclerView.ViewHolder(containerView), View.OnClickListener, LayoutContainer {

    private lateinit var song: Song

    fun bind(songToBind: Song) {
        song = songToBind
        song_title.text = song.title
    }

    override fun onClick(itemView: View) {
        playlistViewModel.playOrPauseSong(song)
    }

    init {
        containerView.setOnClickListener(this)
    }
}
