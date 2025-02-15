package com.spyker3d.tracksnippetplayer.audioplayer.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object PlaybackStateManager {
    private val _playbackStateFlow = MutableStateFlow(PlaybackState())
    val playbackStateFlow: StateFlow<PlaybackState> = _playbackStateFlow.asStateFlow()

    fun updateState(state: PlaybackState) {
        _playbackStateFlow.value = state
    }
}