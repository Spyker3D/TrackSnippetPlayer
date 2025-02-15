package com.spyker3d.tracksnippetplayer.audioplayer.presentation

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AudioPlayerViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    val playbackState: StateFlow<PlaybackState> = PlaybackStateManager.playbackStateFlow

    private fun sendCommandToService(
        action: String,
        extraLong: Pair<String, Long>? = null,
        extraString: Pair<String, String>? = null,
    ) {
        val intent = Intent(context, AudioPlayerService::class.java).apply {
            this.action = action
            extraString?.let { putExtra(it.first, it.second) }
            extraLong?.let { putExtra(it.first, it.second) }
        }
        ContextCompat.startForegroundService(context, intent)
    }

    fun prepareTrack(trackUrl: String) {
        sendCommandToService(AudioPlayerService.ACTION_PREPARE, extraString = "TRACK_URL" to trackUrl)
    }

    fun playPause() {
        val action = if (playbackState.value.isPlaying) {
            AudioPlayerService.ACTION_PAUSE
        } else {
            AudioPlayerService.ACTION_PLAY
        }
        sendCommandToService(action)
    }

    fun rewind() {
        sendCommandToService(AudioPlayerService.ACTION_REWIND)
    }

    fun fastForward() {
        sendCommandToService(AudioPlayerService.ACTION_FAST_FORWARD)
    }

    fun seekTo(position: Long) {
        sendCommandToService(AudioPlayerService.ACTION_SEEK_TO, extraLong = "SEEK POSITION" to position)
    }
}