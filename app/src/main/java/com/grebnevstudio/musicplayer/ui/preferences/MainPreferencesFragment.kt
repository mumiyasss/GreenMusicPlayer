package com.grebnevstudio.musicplayer.ui.preferences

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.grebnevstudio.musicplayer.R
import kotlinx.android.synthetic.main.fragment_preferences.view.*

class MainPreferencesFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val globalView = inflater.inflate(R.layout.fragment_preferences, container, false)

        (activity as AppCompatActivity).setSupportActionBar(globalView.my_toolbar as Toolbar)
        (activity as AppCompatActivity).supportActionBar?.apply {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
        return globalView
    }
}
