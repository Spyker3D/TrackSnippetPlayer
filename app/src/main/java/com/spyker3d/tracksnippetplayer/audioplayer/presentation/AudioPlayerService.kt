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
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.Player
import com.spyker3d.tracksnippetplayer.R
import com.spyker3d.tracksnippetplayer.common.domain.model.Track
import com.spyker3d.tracksnippetplayer.root.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

const val TRACK_URL = "TRACK_URL"
const val TRACK_NAME = "TRACK_NAME"
const val ARTIST_NAME = "ARTIST_NAME"
const val SEEK_POSITION = "SEEK_POSITION"
const val TRACK_ID = "TRACK_ID"
const val IS_DOWNLOADS_SCREEN = "IS_DOWNLOADS_SCREEN"
const val OPEN_AUDIO_PLAYER = "OPEN_AUDIO_PLAYER_NOTIFICATION"
const val TRACK_ID_NOTIFICATION = "TRACK_ID_NOTIFICATION"
const val TRACK_PREVIEW_URL_NOTIFICATION = "TRACK_PREVIEW_URL_NOTIFICATION"
const val IS_DOWNLOADS_SCREEN_NOTIFICATION = "IS_DOWNLOADS_SCREEN_NOTIFICATION"
const val TRACK_LIST = "TRACK_LIST"

class AudioPlayerService : LifecycleService() {
    private lateinit var exoPlayer: ExoPlayer
    private lateinit var mediaSession: MediaSessionCompat

    private var trackName = ""
    private var artistName = ""
    private var lastTrackUrl = ""
    private var trackId: Long = 0
    private var isDownloadsScreen: Boolean = false
    private var playlist: List<Track> = emptyList()
    private var currentIndex: Int = 0
    private var trackUrl = ""

    override fun onCreate() {
        super.onCreate()
        mediaSession = MediaSessionCompat(this, "AudioPlayerService")

        exoPlayer = ExoPlayer.Builder(this).build()
        exoPlayer.shuffleModeEnabled = false
        exoPlayer.repeatMode = Player.REPEAT_MODE_OFF

        exoPlayer.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                updatePlaybackState()
            }

            override fun onPlaybackStateChanged(state: Int) {
                updatePlaybackState()
                updateNotification()
            }
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                currentIndex = exoPlayer.currentMediaItemIndex
                updateTrackInfoFromPlaylist()
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

    /**
     * Обновляение информации о текущем треке из плейлиста.
     * Если плейлист не пустой и текущий индекс корректен, берётся название трека и имя исполнителя.
     */
    private fun updateTrackInfoFromPlaylist() {
        if (playlist.isNotEmpty() && currentIndex in playlist.indices) {
            val currentTrack = playlist[currentIndex]
            trackName = currentTrack.name
            artistName = currentTrack.artistName
            updateNotification()
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
        if (playlist.isNotEmpty() && currentIndex in playlist.indices) {
            val currentTrack = playlist[currentIndex]
            trackName = currentTrack.name
            artistName = currentTrack.artistName
            PlaybackStateManager.updateState(
                PlaybackState(
                    isPlaying = exoPlayer.isPlaying,
                    currentPosition = exoPlayer.currentPosition,
                    duration = if (exoPlayer.duration > 0) exoPlayer.duration else 0L,
                    trackName = trackName,
                    artistName = currentTrack.artistName,
                    trackIndex = currentIndex,
                    albumImage = currentTrack.albumImageMedium,
                    trackTime = currentTrack.duration,
                    albumName = currentTrack.albumName,
                    currentTrack = currentTrack
                )
            )
        }
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        when (intent?.action) {
            ACTION_PREPARE -> {
                trackUrl = intent.getStringExtra(TRACK_URL) ?: ""
                trackName = intent.getStringExtra(TRACK_NAME) ?: ""
                artistName = intent.getStringExtra(ARTIST_NAME) ?: ""
                trackId = intent.getLongExtra(TRACK_ID, 0)
                isDownloadsScreen = intent.getBooleanExtra(IS_DOWNLOADS_SCREEN, false)

                if (!trackName.isNullOrEmpty() && playlist.isNotEmpty()) {
                    exoPlayer.seekTo(playlist.indexOfFirst { it.name == trackName }, 0L)
                }
            }

            ACTION_PREPARE_PLAYLIST -> {
                exoPlayer.playWhenReady = false
                playlist = intent.getParcelableArrayListExtra<Track>(TRACK_LIST) ?: emptyList()
                if (playlist.isNotEmpty()) {
                    currentIndex = 0
                    // Обновляется уведомление для первого трека
                    updateTrackInfoFromPlaylist()
                    // Формируется список MediaItem и устанавливаем их в плеер
                    val mediaItems = playlist.map { track ->
                        MediaItem.Builder()
                            .setUri(track.audioPreview) // или другой URL, по которому воспроизводится трек
                            .setMediaMetadata(
                                MediaMetadata.Builder()
                                    .setTitle(track.name)
                                    .setArtist(track.artistName)
                                    .build()
                            )
                            .build()
                    }
                    exoPlayer.setMediaItems(mediaItems)
                    exoPlayer.prepare()
                }
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

        // Задаем contentIntent для открытия приложения при нажатии на уведомление в свернутом состоянии
        val contentIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java).apply {
                action = Intent.ACTION_MAIN
                addCategory(Intent.CATEGORY_LAUNCHER)
                // Передаем аргументы для открытия экрана аудиоплеера
                putExtra(OPEN_AUDIO_PLAYER, true)
                putExtra(TRACK_ID_NOTIFICATION, trackId)
                putExtra(TRACK_PREVIEW_URL_NOTIFICATION, lastTrackUrl)
                putExtra(IS_DOWNLOADS_SCREEN_NOTIFICATION, isDownloadsScreen)
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(trackName)
            .setContentText(artistName)
            .setSmallIcon(R.drawable.ic_music_note)
            .setContentIntent(contentIntent)
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
        const val ACTION_PREPARE_PLAYLIST =
            "com.spyker3d.tracksnippetplayer.audioplayer.presentation.ACTION_PREPARE_PLAYLIST"
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