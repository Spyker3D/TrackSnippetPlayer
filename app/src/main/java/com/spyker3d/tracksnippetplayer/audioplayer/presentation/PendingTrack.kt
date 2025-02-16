package com.spyker3d.tracksnippetplayer.audioplayer.presentation

data class PendingTrack(
    val trackUrl: String,
    val trackName: String,
    val artistName: String,
    val trackId: Long,
    val isDownloadsScreen: Boolean
)
