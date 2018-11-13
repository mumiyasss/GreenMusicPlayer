package com.grebnevstudio.musicplayer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.grebnevstudio.musicplayer.helpers.openFileIntent
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var playerViewModel: PlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        playerViewModel = ViewModelProviders.of(this).get(PlayerViewModel::class.java)

        playerViewModel.isPlaying.observe(this, Observer { isPlaying ->
            play_pause_btn.text =
                    if (isPlaying) getString(R.string.pause) else getString(R.string.play)
        })

        play_pause_btn.setOnClickListener {
            playerViewModel.playPauseSong()
        }
        stop_service_btn.setOnClickListener {
            playerViewModel.stopService()
        }
        open_fm_button.setOnClickListener {
            startActivityForResult(openFileIntent, READ_REQUEST_CODE)
        }
    }

    public override fun onActivityResult(reqCode: Int, resCode: Int, resultData: Intent?) {
        if (reqCode == READ_REQUEST_CODE && resCode == Activity.RESULT_OK) {
            val uri = resultData?.data
            if (uri != null)
                playerViewModel.uploadFile(uri)
        }
    }

    companion object {
        const val READ_REQUEST_CODE = 99
    }
}
