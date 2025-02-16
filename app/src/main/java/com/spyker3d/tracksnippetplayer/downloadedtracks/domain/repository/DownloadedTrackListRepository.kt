package com.spyker3d.tracksnippetplayer.downloadedtracks.domain.repository

import com.spyker3d.tracksnippetplayer.common.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface DownloadedTrackListRepository {
    fun getAllDownloadedTracks(): Flow<List<Track>>
}