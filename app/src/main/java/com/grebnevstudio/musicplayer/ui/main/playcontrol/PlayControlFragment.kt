package com.grebnevstudio.musicplayer.ui.main.playcontrol

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.grebnevstudio.musicplayer.R
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
        playerViewModel = arguments!!.getSerializable(ARG_VIEW_MODEL_ID) as PlayerViewModel

        with(globalView) {
            playerViewModel.isPlaying.observe(this@PlayControlFragment, Observer { isPlaying ->
               // play_pause_btn.text = if (isPlaying) getString(R.string.pause) else getString(R.string.play)
            })

            playerViewModel.activeSongTitle.observe(this@PlayControlFragment, Observer { songTitle ->
                active_song_title.text = songTitle
            })

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

    companion object {
        private const val ARG_VIEW_MODEL_ID = "vm"

        fun newInstance(viewModel: PlayerViewModel): PlayControlFragment {
            val args = Bundle()
            args.putSerializable(ARG_VIEW_MODEL_ID, viewModel)
            val fragment = PlayControlFragment()
            fragment.arguments = args
            return fragment
        }
    }

}
