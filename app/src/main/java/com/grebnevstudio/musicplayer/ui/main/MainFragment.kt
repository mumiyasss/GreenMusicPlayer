package com.grebnevstudio.musicplayer.ui.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.grebnevstudio.musicplayer.R
import com.grebnevstudio.musicplayer.helpers.openFileIntent
import com.grebnevstudio.musicplayer.helpers.openFolderIntent
import com.grebnevstudio.musicplayer.service.PlayerServiceConnection
import com.grebnevstudio.musicplayer.ui.AppActivity
import com.grebnevstudio.musicplayer.ui.preferences.MainPreferencesFragment
import com.grebnevstudio.musicplayer.viewmodel.PlayerViewModel
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainFragment : Fragment() {
    private lateinit var playerViewModel: PlayerViewModel
    private val serviceConnection = PlayerServiceConnection.get()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val globalView = inflater.inflate(R.layout.fragment_main, container, false)
        (activity as AppCompatActivity).setSupportActionBar(globalView.my_toolbar as Toolbar)
        playerViewModel = ViewModelProviders.of(this).get(PlayerViewModel::class.java)
        serviceConnection.connectService()
        with(globalView) {
            playerViewModel.isPlaying.observe(this@MainFragment, Observer { isPlaying ->
                play_pause_btn.text =
                        if (isPlaying) getString(R.string.pause) else getString(R.string.play)
            })
            play_pause_btn.setOnClickListener {
                playPause()
            }
            stop_service_btn.setOnClickListener {
                if (serviceConnection.bounded)
                    serviceConnection.disconnectService()
            }
            open_fm_button.setOnClickListener {
                startActivityForResult(
                    openFileIntent,
                    UPLOAD_FILE_CODE
                )
            }
            pref_btn.setOnClickListener {
                (activity as AppActivity).startScreen(MainPreferencesFragment())
            }
            open_folder_btn.setOnClickListener {
                startActivityForResult(
                    openFolderIntent,
                    UPLOAD_FOLDER_CODE
                )
            }
        }
        return globalView
    }

    override fun onActivityResult(reqCode: Int, resCode: Int, resultData: Intent?) {
        if (resCode == Activity.RESULT_OK && resultData != null) {
            val responseUri = resultData.data
            if (responseUri != null) when (reqCode) {
                UPLOAD_FILE_CODE -> uploadNewFile(responseUri)
                UPLOAD_FOLDER_CODE -> {
                    val documentFile = DocumentFile.fromTreeUri(activity as Context, responseUri)
                    documentFile?.listFiles()?.forEach {
                        when {
                            it.isDirectory -> logg(it.name + " is dir") // Рекурсивная загрузка
                            it.type == "audio/mpeg" -> logg(it.name + " IS AUDIO FILE")
                            else -> logg(it.name.toString())
                        }
                    }
                }
            }
        }
    }

    private fun logg(str: String) {
        Log.d("HII", str)
    }

    private fun uploadNewFile(uri: Uri) {
        GlobalScope.launch(Dispatchers.Main) {
            initServiceIfNeeded()
            serviceConnection.playerBinder.uploadNewFile(uri)
            playPause()
        }
    }

    private fun playPause() {
        GlobalScope.launch(Dispatchers.Main) {
            initServiceIfNeeded()
            serviceConnection.playerBinder.playOrPause()
            playerViewModel.onPlayPause()
        }
    }

    private suspend fun initServiceIfNeeded() {
        if (!serviceConnection.bounded) {
            serviceConnection.connectService()
            while (!serviceConnection.bounded) {
                delay(10)
            }
        }
    }

    companion object {
        const val UPLOAD_FILE_CODE = 99
        const val UPLOAD_FOLDER_CODE = 98
    }
}
