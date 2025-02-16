package com.spyker3d.tracksnippetplayer.audioplayer.presentation

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.spyker3d.tracksnippetplayer.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

const val TRACK_URL = "TRACK_URL"
const val TRACK_NAME = "TRACK_NAME"
const val ARTIST_NAME = "ARTIST_NAME"
const val SEEK_POSITION = "SEEK_POSITION"

class AudioPlayerService : LifecycleService() {
    private lateinit var exoPlayer: ExoPlayer
    private lateinit var mediaSession: MediaSessionCompat

    private var trackName = ""
    private var artistName = ""
    private var lastTrackUrl = ""

    override fun onCreate() {
        super.onCreate()
        mediaSession = MediaSessionCompat(this, "AudioPlayerService")

        exoPlayer = ExoPlayer.Builder(this).build()

        exoPlayer.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                updatePlaybackState()
            }

            override fun onPlaybackStateChanged(state: Int) {
                updatePlaybackState()
                updateNotification()
            }
        })

        lifecycleScope.launch {
            while (isActive) {
                updatePlaybackState()
                delay(100)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Audio Player",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun updateNotification() {
        val notification = createNotification()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, notification)
            } else {
                Log.w(
                    "AudioPlayerService",
                    "POST_NOTIFICATIONS permission not granted. Requesting permission from Activity."
                )
            }
        } else {
            NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, notification)
        }
    }

    private fun updatePlaybackState() {
        val state = PlaybackState(
            isPlaying = exoPlayer.isPlaying,
            currentPosition = exoPlayer.currentPosition,
            duration = if (exoPlayer.duration > 0) exoPlayer.duration else 0L
        )
        PlaybackStateManager.updateState(state)
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        when (intent?.action) {
            ACTION_PREPARE -> {
                intent.getStringExtra(TRACK_URL)?.let { trackUrl ->
                    val mediaItem = MediaItem.fromUri(trackUrl)
                    exoPlayer.setMediaItem(mediaItem)
//                    exoPlayer.playWhenReady = false
                    exoPlayer.prepare()
                }
                trackName = intent.getStringExtra(TRACK_NAME) ?: ""
                artistName = intent.getStringExtra(ARTIST_NAME) ?: ""
            }

            ACTION_PLAY -> {
                if (exoPlayer.duration > 0 && exoPlayer.currentPosition >= exoPlayer.duration) {
                    exoPlayer.seekTo(0)
                }
                exoPlayer.play()
            }

            ACTION_PAUSE -> {
                exoPlayer.pause()
            }

            ACTION_REWIND -> {
                exoPlayer.seekTo(maxOf(0, exoPlayer.currentPosition - 10_000L))
            }

            ACTION_FAST_FORWARD -> {
                exoPlayer.seekTo(exoPlayer.currentPosition + 10_000L)
            }

            ACTION_STOP -> {
                stopForeground(true)
                stopSelf()
            }

            ACTION_SEEK_TO -> {
                val position = intent.getLongExtra(SEEK_POSITION, 0L)
                exoPlayer.seekTo(position)
            }
        }
        val notification = createNotification()
        startForeground(NOTIFICATION_ID, notification)
        return START_STICKY
    }

    private fun createNotification(): Notification {
        val rewindAction = NotificationCompat.Action(
            R.drawable.png_rewind,
            "Rewind",
            PendingIntent.getService(
                this, 1,
                Intent(this, AudioPlayerService::class.java).apply { action = ACTION_REWIND },
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        )

        val playPauseAction = if (exoPlayer.isPlaying) {
            NotificationCompat.Action(
                R.drawable.png_pause,
                "Pause",
                PendingIntent.getService(
                    this, 0,
                    Intent(this, AudioPlayerService::class.java).apply { action = ACTION_PAUSE },
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
        } else {
            NotificationCompat.Action(
                R.drawable.png_play,
                "Play",
                PendingIntent.getService(
                    this, 0,
                    Intent(this, AudioPlayerService::class.java).apply { action = ACTION_PLAY },
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
        }

        val fastForwardAction = NotificationCompat.Action(
            R.drawable.png_fast_forward,
            "Fast Forward",
            PendingIntent.getService(
                this, 2,
                Intent(this, AudioPlayerService::class.java).apply { action = ACTION_FAST_FORWARD },
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(trackName)
            .setContentText(artistName)
            .setSmallIcon(R.drawable.ic_music_note)
            .addAction(rewindAction)
            .addAction(playPauseAction)
            .addAction(fastForwardAction)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSession.sessionToken)
                    .setShowActionsInCompactView(0, 1, 2)
            )
            .build()
    }

    override fun onDestroy() {
        exoPlayer.release()
        mediaSession.release()
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

    companion object {
        const val ACTION_PREPARE =
            "com.spyker3d.tracksnippetplayer.audioplayer.presentation.ACTION_PREPARE"
        const val ACTION_PLAY =
            "com.spyker3d.tracksnippetplayer.audioplayer.presentation.ACTION_PLAY"
        const val ACTION_PAUSE =
            "com.spyker3d.tracksnippetplayer.audioplayer.presentation.ACTION_PAUSE"
        const val ACTION_REWIND =
            "com.spyker3d.tracksnippetplayer.audioplayer.presentation.ACTION_REWIND"
        const val ACTION_FAST_FORWARD =
            "com.spyker3d.tracksnippetplayer.audioplayer.presentation.ACTION_FAST_FORWARD"
        const val ACTION_STOP = "com.spyker3d.tracksnippetplayer.ACTION_STOP"
        const val ACTION_SEEK_TO = "com.spyker3d.tracksnippetplayer.ACTION_SEEK_TO"
        const val CHANNEL_ID = "audio_player_channel"
        const val NOTIFICATION_ID = 1
    }
}