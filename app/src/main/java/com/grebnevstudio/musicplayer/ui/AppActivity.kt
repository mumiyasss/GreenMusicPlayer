package com.grebnevstudio.musicplayer.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.grebnevstudio.musicplayer.R
import com.grebnevstudio.musicplayer.helpers.baseConfig
import com.grebnevstudio.musicplayer.ui.main.MainFragment

class AppActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(baseConfig.appTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_container)
        startScreen(MainFragment())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> onBackPressed()
            else -> {}
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0)
            super.onBackPressed()
        else
            supportFragmentManager.popBackStack()
    }

    fun startScreen(newFragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, newFragment)
            .addToBackStack(null)
            .commit()
    }

    fun applyNewTheme(): Boolean {
        recreate()
        return true
    }
}
