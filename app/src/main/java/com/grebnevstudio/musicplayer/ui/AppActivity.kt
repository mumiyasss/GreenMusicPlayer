package com.grebnevstudio.musicplayer.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.grebnevstudio.musicplayer.R
import com.grebnevstudio.musicplayer.ui.main.MainFragment

class AppActivity : AppCompatActivity() {

    fun startScreen(newFragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, newFragment).commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_container)

        startScreen(MainFragment())
    }

}