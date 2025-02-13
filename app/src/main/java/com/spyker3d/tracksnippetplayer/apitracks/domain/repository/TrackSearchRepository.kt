package com.spyker3d.tracksnippetplayer.apitracks.domain.repository

import com.spyker3d.tracksnippetplayer.apitracks.domain.model.Track

interface TrackSearchRepository {
    suspend fun searchTrack(request: String): List<Track>
}