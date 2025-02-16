package com.spyker3d.tracksnippetplayer.search.domain.repository

import com.spyker3d.tracksnippetplayer.common.domain.model.Track

interface TrackSearchRepository {
    suspend fun searchTrack(request: String): List<Track>

    suspend fun getTrackById(trackId: Int): Track
}