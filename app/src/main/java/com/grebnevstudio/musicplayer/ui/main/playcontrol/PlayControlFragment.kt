package com.grebnevstudio.musicplayer.ui.main.playcontrol

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.grebnevstudio.musicplayer.R
import com.grebnevstudio.musicplayer.helpers.asyncOnMainThread
import com.grebnevstudio.musicplayer.viewmodel.PlayerViewModel
import kotlinx.android.synthetic.main.ui_fragment_playcontroller.view.*

class PlayControlFragment : Fragment() {
    private lateinit var playerViewModel: PlayerViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val globalView = inflater.inflate(R.layout.ui_fragment_playcontroller, container, false)
        playerViewModel = ViewModelProviders.of(this).get(PlayerViewModel::class.java)

        with(globalView) {
            asyncOnMainThread {
                playerViewModel.isPlayingStatus().observe(this@PlayControlFragment, Observer { isPlaying ->
                    // play_pause_btn.text = if (isPlaying) getString(R.string.pause) else getString(R.string.play)
                })
                playerViewModel.getActiveSong().observe(this@PlayControlFragment, Observer { song ->
                    active_song_title.text = song.name
                })
            }
            play_pause_btn.setOnClickListener {
                playerViewModel.playOrPauseSong()
            }
            next_btn.setOnClickListener {
                playerViewModel.playNext()
            }
            previous_btn.setOnClickListener {
                playerViewModel.playPrevious()
            }
        }
        return globalView
    }
}
