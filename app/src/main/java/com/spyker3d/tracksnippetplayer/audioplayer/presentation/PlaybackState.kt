package com.spyker3d.tracksnippetplayer.audioplayer.presentation

import com.spyker3d.tracksnippetplayer.common.domain.model.Track

data class PlaybackState(
    val isPlaying: Boolean = false,
    val currentPosition: Long = 0L,
    val duration: Long = 0L,
    val trackName: String = "",
    val artistName: String = "",
    val trackIndex: Int = 0,
    val albumImage: String = "",
    val trackTime: String = "",
    val albumName: String = "",
    val currentTrack: Track? = null
)