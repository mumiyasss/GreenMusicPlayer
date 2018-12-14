package com.grebnevstudio.musicplayer.ui.main.playcontrol

import android.graphics.drawable.Animatable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.grebnevstudio.musicplayer.R
import com.grebnevstudio.musicplayer.extensions.toMinutesSecondsFormat
import com.grebnevstudio.musicplayer.helpers.asyncOnMainThread
import com.grebnevstudio.musicplayer.viewmodel.PlayerViewModel
import kotlinx.android.synthetic.main.ui_fragment_playcontroller.view.*

class PlayControlFragment : Fragment() {
    private lateinit var playerViewModel: PlayerViewModel
    private lateinit var globalView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playerViewModel = ViewModelProviders.of(this).get(PlayerViewModel::class.java)
        lifecycle.addObserver(playerViewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        globalView = inflater.inflate(R.layout.ui_fragment_playcontroller, container, false)
        with(globalView) {
            play_pause_btn.setOnClickListener { playerViewModel.playOrPauseSong()
            }
            next_btn.setOnClickListener {
                playerViewModel.playNext()
            }
            previous_btn.setOnClickListener {
                playerViewModel.playPrevious()
            }
            shuffle_btn.setOnClickListener {
                playerViewModel.onShufflePressed()
            }
            repeat_btn.setOnClickListener {
                playerViewModel.onRepeatPressed()
            }
            seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {}

                override fun onStartTrackingTouch(seekBar: SeekBar) {}

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    if (fromUser) playerViewModel.seekTo(progress)
                }
            })
        }
        return globalView
    }

    override fun onResume() {
        super.onResume()
        with(globalView) {
            asyncOnMainThread {
                playerViewModel.playingStatus().observe(this@PlayControlFragment, Observer { playing ->
                    play_pause_btn.setImageResource(
                        if (playing)
                            R.drawable.anim_play_to_pause
                        else R.drawable.anim_pause_to_play
                    )
                    (play_pause_btn.drawable as Animatable).start()
                })
                playerViewModel.getActiveSong().observe(this@PlayControlFragment, Observer { song ->
                    active_song_title.text = song.title
                    active_song_artist.text = song.artist
                    active_song_duration.text = song.duration.toMinutesSecondsFormat()
                    seek_bar.max = song.duration
                })
                playerViewModel.currentPosition.observe(this@PlayControlFragment, Observer { position ->
                    seek_bar.progress = position
                    how_long_is_playing.text = position.toMinutesSecondsFormat()
                })
                playerViewModel.repeatMode.observe(this@PlayControlFragment, Observer { repeatMode ->
                    repeat_btn.setImageResource(
                        if (repeatMode) R.drawable.ic_repeat_active else R.drawable.ic_repeat_disabled
                    )
                })
                playerViewModel.shuffleMode.observe(this@PlayControlFragment, Observer { shuffleMode ->
                    shuffle_btn.setImageResource(
                        if (shuffleMode) R.drawable.ic_shuffle_active else R.drawable.ic_shuffle_disabled
                    )
                })
            }
        }
    }
}
