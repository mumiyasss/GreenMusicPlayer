package com.grebnevstudio.musicplayer.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.grebnevstudio.musicplayer.R
import com.grebnevstudio.musicplayer.ui.main.MainFragment

class AppActivity : AppCompatActivity() {
    private val fragmentContainerId = R.id.fragment_container

    override fun onCreate(savedInstanceState: Bundle?) {
        //setTheme(baseConfig.appTheme)
        setTheme(R.style.AppThemeDefault)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.container_app_activity)
        setupFirstFragmentIfNeeded(MainFragment())
    }

    private fun setupFirstFragmentIfNeeded(screenToLaunch: Fragment) {
        if (supportFragmentManager.findFragmentById(fragmentContainerId) == null)
            startScreen(screenToLaunch)
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
            .replace(fragmentContainerId, newFragment)
            .addToBackStack(null)
            .commit()
    }

    fun applyNewTheme(): Boolean {
        recreate()
        return true
    }
}
