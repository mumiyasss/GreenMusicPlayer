package com.grebnevstudio.musicplayer.ui.main.playlist

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.grebnevstudio.musicplayer.ExpandIconClickListener
import com.grebnevstudio.musicplayer.R
import com.grebnevstudio.musicplayer.helpers.logg
import com.grebnevstudio.musicplayer.ui.AppActivity
import com.grebnevstudio.musicplayer.ui.preferences.MainPreferencesFragment
import com.grebnevstudio.musicplayer.viewmodel.PlaylistViewModel
import kotlinx.android.synthetic.main.backdrop.view.*
import kotlinx.android.synthetic.main.ui_fragment_playlist.view.*

class PlaylistFragment : Fragment() {
    private lateinit var songsAdapter: SongsAdapter
    private lateinit var playlistViewModel: PlaylistViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val globalView = inflater.inflate(R.layout.ui_fragment_playlist, container, false)
        playlistViewModel = ViewModelProviders.of(this).get(PlaylistViewModel::class.java)

        with(globalView) {
            (activity as AppCompatActivity).setSupportActionBar(app_bar)
            app_bar.expand_icon.setOnClickListener(
                ExpandIconClickListener(
                    activity as AppCompatActivity,
                    nested_scroll_view,
                    AccelerateDecelerateInterpolator(),
                    ContextCompat.getDrawable(context!!, R.drawable.ic_expand_more_black_24dp),
                    ContextCompat.getDrawable(context!!, R.drawable.ic_expand_less_black_24dp)
                )
            )

            songsAdapter = SongsAdapter(
                activity as AppActivity,
                playlistViewModel
            )
            songs_list.layoutManager = LinearLayoutManager(activity)
            songs_list.adapter = songsAdapter

            playlistViewModel.getSongs().observe(this@PlaylistFragment, Observer { songs ->
                playlistViewModel.onSongsSetChanged(songs)
                songsAdapter.songs = songs
            })

            clear_playlist_btn.setOnClickListener {
                playlistViewModel.clearPlaylist()
            }

//            add_songs_btn.setOnClickListener {
//                startActivityForResult(
//                    openFolderIntent,
//                    UPLOAD_FOLDER_CODE
//                )
//            }

            settings_btn.setOnClickListener {
                (activity as AppActivity).startScreen(MainPreferencesFragment())
            }

        }

        // Set cut corner background for API 23+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            globalView.nested_scroll_view.background = context?.getDrawable(R.drawable.background_cut_corners)
        }
        return globalView
    }

//    override fun onActivityResult(reqCode: Int, resCode: Int, resultData: Intent?) {
//        if (resCode == Activity.RESULT_OK && resultData != null) {
//            resultData.data?.let { responseUri ->
//                when (reqCode) {
//                    UPLOAD_FOLDER_CODE -> playlistViewModel.uploadNewFolder(treeUri = responseUri)
//                }
//            }
//        }
//    }

    companion object {
        private const val UPLOAD_FOLDER_CODE = 98
    }
}

