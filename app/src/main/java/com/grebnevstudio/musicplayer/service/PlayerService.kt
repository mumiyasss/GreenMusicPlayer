package com.grebnevstudio.musicplayer.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import androidx.core.app.NotificationCompat
import com.grebnevstudio.musicplayer.R
import com.grebnevstudio.musicplayer.helpers.ACTION_NEXT
import com.grebnevstudio.musicplayer.helpers.ACTION_PLAY_PAUSE
import com.grebnevstudio.musicplayer.helpers.ACTION_PREVIOUS
import com.grebnevstudio.musicplayer.helpers.isOreoPlus
import com.grebnevstudio.musicplayer.reciever.ControlActionsListener
import com.grebnevstudio.musicplayer.ui.main.MainFragment

class PlayerService : Service() {
    private val mediaPlayer by lazy {
        MediaPlayer().apply {
            setAudioStreamType(AudioManager.STREAM_MUSIC);
        }
    }
    private val mBinder = PlayerServiceBinder()
    private var prepared = false

    private fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    private fun playOrPause() {
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

    private fun uploadNewFile(uri: Uri) {
        if (prepared) {
            prepared = false
            mediaPlayer.reset()
        }
        mediaPlayer.setDataSource(this, uri)
    }

    inner class PlayerServiceBinder : Binder() {
        fun isPlaying(): Boolean = this@PlayerService.isPlaying()
        fun playOrPause() = this@PlayerService.playOrPause()
        fun uploadNewFile(uri: Uri) = this@PlayerService.uploadNewFile(uri)
        fun stopService() = this@PlayerService.stopForegroundService()
    }

    override fun onBind(intent: Intent) = mBinder

    private fun setup() {
        setupNotification()
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
        val contentIntent = Intent(this, MainFragment::class.java)
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
        if (isOreoPlus()) {
            val name = context.getString(R.string.channel_name)
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = getString(R.string.channel_description)
            }
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val CHANNEL_ID = "MUSIC_PLAYER_CHANNEL_ID"
    }
}