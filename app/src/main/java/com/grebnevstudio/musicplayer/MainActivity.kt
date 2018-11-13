package com.grebnevstudio.musicplayer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*
import android.app.Activity


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
            val openFileIntent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "audio/*"
            }
            startActivityForResult(openFileIntent, READ_REQUEST_CODE)
        }
    }

    public override fun onActivityResult(
        requestCode: Int, resultCode: Int,
        resultData: Intent?
    ) {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val uri = resultData?.data
            if (uri != null)
                playerViewModel.uploadFile(uri)
        }
    }

    companion object {
        const val READ_REQUEST_CODE = 99;
    }
}
