package com.grebnevstudio.musicplayer.ui.main.playcontrol

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.grebnevstudio.musicplayer.R
import com.grebnevstudio.musicplayer.helpers.openFolderIntent
import com.grebnevstudio.musicplayer.ui.AppActivity
import com.grebnevstudio.musicplayer.ui.preferences.MainPreferencesFragment
import com.grebnevstudio.musicplayer.viewmodel.PlayerViewModel
import kotlinx.android.synthetic.main.fragment_main.view.*

class PlayControlFragment : Fragment() {
    private lateinit var playerViewModel: PlayerViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val globalView = inflater.inflate(R.layout.fragment_main, container, false)
        playerViewModel = ViewModelProviders.of(this).get(PlayerViewModel::class.java)

        with(globalView) {
            playerViewModel.isPlaying.observe(this@PlayControlFragment, Observer { isPlaying ->
                play_pause_btn.text = if (isPlaying) getString(R.string.pause) else getString(R.string.play)
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
            stop_service_btn.setOnClickListener {
                //playerViewModel.playNext()
            }
            pref_btn.setOnClickListener {
                (activity as AppActivity).startScreen(MainPreferencesFragment())
            }
            open_folder_btn.setOnClickListener {
                startActivityForResult(
                    openFolderIntent,
                    UPLOAD_FOLDER_CODE
                )
            }
            clear_song_list_btn.setOnClickListener {
                playerViewModel.clearPlaylist()
            }
        }
        return globalView
    }

    override fun onActivityResult(reqCode: Int, resCode: Int, resultData: Intent?) {
        if (resCode == Activity.RESULT_OK && resultData != null) {
            resultData.data?.let { responseUri ->
                when (reqCode) {
                    UPLOAD_FOLDER_CODE -> playerViewModel.uploadNewFolder(treeUri = responseUri)
                }
            }
        }
    }

    companion object {
        const val UPLOAD_FOLDER_CODE = 98
    }
}
