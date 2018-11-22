package com.grebnevstudio.musicplayer.ui.preferences

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.grebnevstudio.musicplayer.R
import com.grebnevstudio.musicplayer.helpers.APP_THEME
import com.grebnevstudio.musicplayer.helpers.baseConfig
import com.grebnevstudio.musicplayer.ui.AppActivity

class ListPreferencesFragment : PreferenceFragmentCompat() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.settings)
        preferenceManager.findPreference(APP_THEME).setOnPreferenceChangeListener { _, newValue ->
            (activity as AppActivity).baseConfig.appTheme = when (newValue) {
                "1" -> R.style.AppThemeDefault
                "2" -> R.style.AppThemeDark
                "3" -> R.style.AppThemeAmoled
                else -> R.style.AppThemeDefault
            }
            (activity as AppActivity).applyNewTheme()
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    }
}