package com.grebnevstudio.musicplayer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.grebnevstudio.musicplayer.helpers.openFileIntent
import com.grebnevstudio.musicplayer.viewmodels.PlayerViewModel
import kotlinx.android.synthetic.main.activity_main.view.*

class MainFragment : Fragment() {
    private lateinit var playerViewModel: PlayerViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val globalView = inflater.inflate(R.layout.activity_main, container, false)

        playerViewModel = ViewModelProviders.of(this).get(PlayerViewModel::class.java)

        with(globalView) {
            playerViewModel.isPlaying.observe(this@MainFragment, Observer { isPlaying ->
                play_pause_btn.text =
                        if (isPlaying) getString(R.string.pause) else getString(R.string.play)
            })

            play_pause_btn.setOnClickListener {
                playerViewModel.playPauseSong()
            }
            stop_service_btn.setOnClickListener {
                playerViewModel.stopService()
            }
            open_fm_button.setOnClickListener {
                startActivityForResult(openFileIntent, READ_REQUEST_CODE)
            }
        }
        return globalView
    }

    override fun onActivityResult(reqCode: Int, resCode: Int, resultData: Intent?) {
        if (reqCode == READ_REQUEST_CODE && resCode == Activity.RESULT_OK) {
            val uri = resultData?.data
            if (uri != null)
                playerViewModel.uploadFile(uri)
        }
    }

    companion object {
        const val READ_REQUEST_CODE = 99
    }
}