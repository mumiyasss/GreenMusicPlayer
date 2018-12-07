package com.grebnevstudio.musicplayer.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProviders
import com.grebnevstudio.musicplayer.R
import com.grebnevstudio.musicplayer.ui.AppActivity
import com.grebnevstudio.musicplayer.ui.main.playcontrol.PlayControlFragment
import com.grebnevstudio.musicplayer.ui.main.playlist.PlaylistFragment
import com.grebnevstudio.musicplayer.viewmodel.PlayerViewModel
import kotlinx.android.synthetic.main.container_viewpager.view.*

class MainViewPagerFragment : Fragment() {

    lateinit var playerViewModel: PlayerViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val globalView = inflater.inflate(R.layout.container_viewpager, container, false)
        globalView.viewpager.adapter =
                MainFragmentPagerAdapter((activity as AppActivity).supportFragmentManager)
        playerViewModel = ViewModelProviders.of(this).get(PlayerViewModel::class.java)
        return globalView
    }

    private inner class MainFragmentPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        // There are only 2 tabs
        override fun getCount() = 2

        override fun getItem(position: Int) =
            when (position) {
                0 -> PlayControlFragment.newInstance(playerViewModel)
                1 -> PlaylistFragment.newInstance(playerViewModel)
                else -> PlayControlFragment.newInstance(playerViewModel)
            }
    }
}
