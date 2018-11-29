package com.grebnevstudio.musicplayer.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import androidx.core.app.NotificationCompat
import com.grebnevstudio.musicplayer.R
import com.grebnevstudio.musicplayer.db.Song
import com.grebnevstudio.musicplayer.helpers.*
import com.grebnevstudio.musicplayer.reciever.ControlActionsListener
import com.grebnevstudio.musicplayer.ui.main.playcontrol.PlayControlFragment
import java.io.IOException

class PlayerService : Service() {
    private var activeSong: Song? = null
    private var songsToPlay: List<Song> = ArrayList()

    private val mediaPlayer by lazy {
        MediaPlayer().apply {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
            setAudioAttributes(audioAttributes)
        }
    }
    private var prepared = false
    private fun isPlaying() = mediaPlayer.isPlaying

    private fun previous() {
        next(indexShift = -1)
    }

    private fun next(indexShift: Int = 1) {
        val indexOfSongToPlay = songsToPlay.indexOf(activeSong) + indexShift
        if (songsToPlay.size > indexOfSongToPlay && indexOfSongToPlay >= 0) {
            playOrPauseSong(songsToPlay[indexOfSongToPlay])
        }
    }

    private fun playOrPauseSong(song: Song?) {
        try {
            if (song == null)
                playOrPause()
            else {
                if (prepared) {
                    prepared = false
                    mediaPlayer.reset()
                }
                mediaPlayer.setDataSource(this, Uri.parse(song.path))
                activeSong = song
                playOrPause()
            }
        } catch (e: IOException) {
            showToast(FILE_NOT_FOUND)
            activeSong = null
            prepared = false
            mediaPlayer.reset()
        }
    }

    private fun playOrPause() {
        if(activeSong != null) {
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
        } else showToast(FILE_NOT_FOUND)
    }

    private val mBinder = PlayerServiceBinder()

    inner class PlayerServiceBinder : Binder() {
        fun isPlaying(): Boolean = this@PlayerService.isPlaying()
        fun next() = this@PlayerService.next()
        fun previous() = this@PlayerService.previous()
        fun playOrPauseSong(song: Song?) = this@PlayerService.playOrPauseSong(song)
        fun stopService() = this@PlayerService.stopForegroundService()
        fun getActiveSong() = this@PlayerService.activeSong
        fun setSongsList(songs: List<Song>) {
            this@PlayerService.songsToPlay = songs
        }
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
        val contentIntent = Intent(this, PlayControlFragment::class.java)
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