package com.spyker3d.tracksnippetplayer.audioplayer.presentation

data class PlaybackState(
    val isPlaying: Boolean = false,
    val currentPosition: Long = 0L,
    val duration: Long = 0L
)