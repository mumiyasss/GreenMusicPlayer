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
import com.grebnevstudio.musicplayer.ui.AppActivity
import com.grebnevstudio.musicplayer.viewmodel.PlayerViewModel
import kotlinx.android.synthetic.main.ui_fragment_playlist.view.*

class PlaylistFragment : Fragment() {
    private lateinit var songsAdapter: SongsAdapter
    private lateinit var playerViewModel: PlayerViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val globalView = inflater.inflate(R.layout.ui_fragment_playlist, container, false)
        playerViewModel = ViewModelProviders.of(this).get(PlayerViewModel::class.java)


        with(globalView) {
            (activity as AppCompatActivity).setSupportActionBar(app_bar)
            app_bar.expand_icon.setOnClickListener(ExpandIconClickListener(
                activity as AppCompatActivity,
                nested_scroll_view,
                AccelerateDecelerateInterpolator(),
                ContextCompat.getDrawable(context!!, R.drawable.ic_expand_more_black_24dp),
                ContextCompat.getDrawable(context!!, R.drawable.ic_expand_less_black_24dp)))

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

        // Set cut corner background for API 23+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            globalView.nested_scroll_view.background = context?.getDrawable(R.drawable.background_cut_corners)
        }
        return globalView
    }
}

