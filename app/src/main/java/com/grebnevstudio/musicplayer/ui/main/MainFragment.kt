package com.grebnevstudio.musicplayer.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.grebnevstudio.musicplayer.PlayerServiceConnection
import com.grebnevstudio.musicplayer.R
import com.grebnevstudio.musicplayer.helpers.openFileIntent
import com.grebnevstudio.musicplayer.ui.AppActivity
import com.grebnevstudio.musicplayer.ui.preferences.Fragment1
import com.grebnevstudio.musicplayer.viewmodel.PlayerViewModel
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainFragment : Fragment() {
    private lateinit var playerViewModel: PlayerViewModel
    private val serviceConnection = PlayerServiceConnection.get()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val globalView = inflater.inflate(R.layout.fragment_main, container, false)
        playerViewModel = ViewModelProviders.of(this).get(PlayerViewModel::class.java)
        serviceConnection.connectService()
        with(globalView) {
            playerViewModel.isPlaying.observe(this@MainFragment, Observer { isPlaying ->
                play_pause_btn.text =
                        if (isPlaying) getString(R.string.pause) else getString(R.string.play)
            })
            play_pause_btn.setOnClickListener {
                playPause()
            }
            stop_service_btn.setOnClickListener {
                if (serviceConnection.bounded)
                    serviceConnection.disconnectService()
            }
            open_fm_button.setOnClickListener {
                startActivityForResult(
                    openFileIntent,
                    READ_REQUEST_CODE
                )
            }
            pref_btn.setOnClickListener {
                (activity as AppActivity).startScreen(Fragment1())
            }
        }
        return globalView
    }

    override fun onActivityResult(reqCode: Int, resCode: Int, resultData: Intent?) {
        if (reqCode == READ_REQUEST_CODE && resCode == Activity.RESULT_OK) {
            val uri = resultData?.data
            if (uri != null) {
                GlobalScope.launch(Dispatchers.Main) {
                    initServiceIfNeeded()
                    serviceConnection.playerBinder.uploadNewFile(uri)
                    playPause()
                }
            }
        }
    }

    private fun playPause() {
        GlobalScope.launch(Dispatchers.Main) {
            initServiceIfNeeded()
            serviceConnection.playerBinder.playOrPause()
            playerViewModel.onPlayPause()
        }
    }

    private suspend fun initServiceIfNeeded() {
        if (!serviceConnection.bounded) {
            serviceConnection.connectService()
            while (!serviceConnection.bounded) {
                delay(10)
            }
        }
    }

    companion object {
        const val READ_REQUEST_CODE = 99
    }
}
