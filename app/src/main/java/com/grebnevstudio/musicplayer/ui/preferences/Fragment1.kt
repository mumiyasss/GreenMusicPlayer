package com.grebnevstudio.musicplayer.ui.preferences

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.grebnevstudio.musicplayer.R

class Fragment1 : PreferenceFragmentCompat() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.settings)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

    }
}