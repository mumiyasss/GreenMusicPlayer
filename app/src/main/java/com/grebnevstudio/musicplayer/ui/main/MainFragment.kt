package com.grebnevstudio.musicplayer.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.grebnevstudio.musicplayer.R
import com.grebnevstudio.musicplayer.helpers.openFolderIntent
import com.grebnevstudio.musicplayer.ui.AppActivity
import com.grebnevstudio.musicplayer.ui.preferences.MainPreferencesFragment
import com.grebnevstudio.musicplayer.viewmodel.PlayerViewModel
import kotlinx.android.synthetic.main.fragment_main.view.*

class MainFragment : Fragment() {
    private lateinit var playerViewModel: PlayerViewModel
    private lateinit var songsAdapter: SongsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val globalView = inflater.inflate(R.layout.fragment_main, container, false)
        (activity as AppCompatActivity).setSupportActionBar(globalView.my_toolbar as Toolbar)
        playerViewModel = ViewModelProviders.of(this).get(PlayerViewModel::class.java)

        with(globalView) {
            songsAdapter = SongsAdapter(activity as AppActivity, playerViewModel)
            songs_list.layoutManager = LinearLayoutManager(activity)
            songs_list.adapter = songsAdapter

            playerViewModel.isPlaying.observe(this@MainFragment, Observer { isPlaying ->
                play_pause_btn.text = if (isPlaying) getString(R.string.pause) else getString(R.string.play)
            })

            playerViewModel.activeSongTitle.observe(this@MainFragment, Observer { songTitle ->
                active_song_title.text = songTitle
            })

            playerViewModel.getSongs().observe(this@MainFragment, Observer { songs ->
                songsAdapter.songs = songs
            })

            play_pause_btn.setOnClickListener {
                playerViewModel.playOrPauseSong()
            }
            stop_service_btn.setOnClickListener {

            }
            pref_btn.setOnClickListener {
                (activity as AppActivity).startScreen(MainPreferencesFragment())
            }
            open_folder_btn.setOnClickListener {
                startActivityForResult(
                    openFolderIntent,
                    MainFragment.UPLOAD_FOLDER_CODE
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
