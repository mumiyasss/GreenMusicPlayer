package com.grebnevstudio.musicplayer.ui.main.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.grebnevstudio.musicplayer.R
import com.grebnevstudio.musicplayer.ui.AppActivity
import com.grebnevstudio.musicplayer.viewmodel.PlayerViewModel
import kotlinx.android.synthetic.main.fragment_playlist.view.*

class PlaylistFragment : Fragment() {
    private lateinit var songsAdapter: SongsAdapter
    private lateinit var playerViewModel: PlayerViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val globalView = inflater.inflate(R.layout.fragment_playlist, container, false)

        playerViewModel = ViewModelProviders.of(this).get(PlayerViewModel::class.java)

        with(globalView) {
            songsAdapter = SongsAdapter(
                activity as AppActivity,
                playerViewModel
            )
            songs_list.layoutManager = LinearLayoutManager(activity)
            songs_list.adapter = songsAdapter


            playerViewModel.getSongs().observe(this@PlaylistFragment, Observer { songs ->
                playerViewModel.onSongsSetChanged(songs)
                songsAdapter.songs = songs
            })

        }
        return globalView
    }
}

