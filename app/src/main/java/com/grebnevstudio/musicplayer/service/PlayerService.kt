package com.grebnevstudio.musicplayer.service

import android.app.*


import android.content.Intent
import android.graphics.BitmapFactory
import android.os.IBinder
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.Build
import androidx.core.app.NotificationCompat
import com.grebnevstudio.musicplayer.ControlActionsListener
import com.grebnevstudio.musicplayer.MainActivity
import com.grebnevstudio.musicplayer.R
import com.grebnevstudio.musicplayer.helpers.*
import kotlin.collections.ArrayList
class PlayerService : Service() {
    private lateinit var mediaPlayer: MediaPlayer
    private val mBinder = PlayerServiceBinder()
    private val songs = ArrayList<Uri>()
    private var prepared = false

    fun isPlaying(): Boolean {
        initMediaPlayerIfNeeded()
        return mediaPlayer.isPlaying
    }

    fun playOrPause() {
        setup()
        with(mediaPlayer) {
            if (isPlaying)
                pause()
            else {
                if (!prepared) {
                    prepare()
                    prepared = true
                }
                start()
            }
        }
    }

    fun uploadNewFile(uri: Uri) {
        songs.add(uri)
        playLastAddedSong()
    }

    inner class PlayerServiceBinder : Binder() {
        val service: PlayerService
            get() = this@PlayerService
    }

    override fun onBind(intent: Intent) = mBinder

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        when (intent.action) {
            ACTION_STOP_FOREGROUND_SERVICE -> {
                stopForegroundService()
            }
            ACTION_PLAY_PAUSE -> playOrPause()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun setup() {
        initMediaPlayerIfNeeded()
        setupNotification()
    }

    private fun initMediaPlayerIfNeeded() {
        if (!::mediaPlayer.isInitialized)
            mediaPlayer = MediaPlayer().apply {
                setAudioStreamType(AudioManager.STREAM_MUSIC);
            }
    }

    private fun setupNotification() {
        val largeIconBitmap = BitmapFactory.decodeResource(resources, R.drawable.album)

        val playOrPauseButton =
            if (isPlaying())
                android.R.drawable.ic_media_play
            else android.R.drawable.ic_media_pause

        createNotificationChannel(this)
        val notification = NotificationCompat.Builder(this, CHANNEL_ID).run {
            setWhen(System.currentTimeMillis())
            setSmallIcon(R.drawable.album)
            setContentTitle("Imperial")
            setContentText("Porchy, ЛСП, Oxxxymiron")
            setOngoing(true)
            setContentIntent(getContentIntent())
            setUsesChronometer(true)
            setLargeIcon(largeIconBitmap)
            priority = if (isOreoPlus()) NotificationManager.IMPORTANCE_HIGH else NotificationCompat.PRIORITY_HIGH
            setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(0, 1, 2)
                    .setShowCancelButton(true)
                //.setMediaSession(mediaSession?.sessionToken)
            )
            addAction(android.R.drawable.ic_media_previous, "Previous", getActionIntent(ACTION_PREVIOUS))
            addAction(playOrPauseButton, "Play / Pause", getActionIntent(ACTION_PLAY_PAUSE))
            addAction(android.R.drawable.ic_media_next, "Next", getActionIntent(ACTION_NEXT))
            build()
        }
        startForeground(1, notification)
    }

    private fun getContentIntent(): PendingIntent {
        val contentIntent = Intent(this, MainActivity::class.java)
        return PendingIntent.getActivity(this, 0, contentIntent, 0)
    }

    private fun getActionIntent(action: String): PendingIntent {
        val intent = Intent(this, ControlActionsListener::class.java)
        intent.action = action
        return PendingIntent.getBroadcast(this, 0, intent, 0)
    }

    private fun stopForegroundService() {
        mediaPlayer.stop()
        mediaPlayer.release()
        stopForeground(true)
        stopSelf()
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.channel_name)
            val descriptionText = context.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun playLastAddedSong() {
        mediaPlayer.setDataSource(this, songs[songs.size - 1])
        playOrPause()
    }

    companion object {
        private const val CHANNEL_ID = "MUSIC_PLAYER_CHANNEL_ID"
    }
}