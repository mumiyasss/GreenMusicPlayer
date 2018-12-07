package com.grebnevstudio.musicplayer.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.grebnevstudio.musicplayer.R
import com.grebnevstudio.musicplayer.ui.AppActivity
import com.grebnevstudio.musicplayer.ui.main.playcontrol.PlayControlFragment
import com.grebnevstudio.musicplayer.ui.main.playlist.PlaylistFragment
import kotlinx.android.synthetic.main.container_viewpager.view.*

class MainFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val globalView = inflater.inflate(R.layout.container_viewpager, container, false)
        globalView.viewpager.adapter =
                MainFragmentPagerAdapter((activity as AppActivity).supportFragmentManager)
        return globalView
    }

    private class MainFragmentPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getCount() = PAGE_COUNT

        override fun getItem(position: Int) =
            when (position) {
                0 -> PlayControlFragment()
                1 -> PlaylistFragment()
                else -> PlayControlFragment()
            }

        companion object {
            private const val PAGE_COUNT = 2
        }
    }
}
