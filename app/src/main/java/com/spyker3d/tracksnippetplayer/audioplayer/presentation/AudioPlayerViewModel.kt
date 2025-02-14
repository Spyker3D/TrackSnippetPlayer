package com.spyker3d.tracksnippetplayer.audioplayer.presentation

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AudioPlayerViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {
    // Создаём экземпляр ExoPlayer и подготавливаем медиапредмет
    var exoPlayer: ExoPlayer = ExoPlayer.Builder(context).build()
        private set

    // Состояние плеера: проигрывается ли, текущая позиция и общая длительность
    var playbackState by mutableStateOf<PlaybackState>(PlaybackState())
        private set

    private val styledPlayerView: StyledPlayerView = StyledPlayerView(context)

    init {
        // Слушатель изменений состояния плеера
        exoPlayer.addListener(object : com.google.android.exoplayer2.Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                updatePlaybackState()
            }
            override fun onPlaybackStateChanged(state: Int) {
                updatePlaybackState()
            }
        })

        // Обновляем состояние плеера периодически (например, раз в 500 мс)
        viewModelScope.launch {
            while (true) {
                updatePlaybackState()
                delay(500)
            }
        }
    }

    fun preparePlayer(uri: String) {
        Log.e("TEST", "uri in viewModel: $uri")
        val mediaItem = MediaItem.fromUri(uri)
        exoPlayer.apply {
            setMediaItem(mediaItem)
            prepare()
        }
    }

    private fun updatePlaybackState() {
        playbackState = PlaybackState(
            isPlaying = exoPlayer.isPlaying,
            currentPosition = exoPlayer.currentPosition,
            duration = if (exoPlayer.duration > 0) exoPlayer.duration else 0L
        )
    }

    fun playPause() {
        Log.e("TEST", "is playing in viewmodel: ${exoPlayer.isPlaying}")
        if (exoPlayer.isPlaying) exoPlayer.pause() else exoPlayer.play()
    }

    fun seekTo(position: Long) {
        exoPlayer.seekTo(position)
    }

    fun fastForward() {
        val newPosition = exoPlayer.currentPosition + 10_000L // перемотка вперёд на 10 сек.
        exoPlayer.seekTo(newPosition)
    }

    fun rewind() {
        val newPosition = exoPlayer.currentPosition - 10_000L // перемотка назад на 10 сек.
        exoPlayer.seekTo(if (newPosition < 0L) 0L else newPosition)
    }

    override fun onCleared() {
        super.onCleared()
        exoPlayer.release()
    }
}