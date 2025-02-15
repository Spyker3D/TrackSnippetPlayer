package com.spyker3d.tracksnippetplayer.apitracks.data.network

import com.spyker3d.tracksnippetplayer.apitracks.data.dto.TrackDto
import com.spyker3d.tracksnippetplayer.apitracks.data.dto.TracksSearchResponse

interface NetworkClient {

    suspend fun searchTrackByName(trackName: String): TracksSearchResponse

    suspend fun searchTrackById(trackId: Int): TrackDto
}