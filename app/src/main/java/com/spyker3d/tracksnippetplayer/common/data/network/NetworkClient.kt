package com.spyker3d.tracksnippetplayer.common.data.network

import com.spyker3d.tracksnippetplayer.common.data.network.dto.TrackDto
import com.spyker3d.tracksnippetplayer.common.data.network.dto.TracksSearchResponse

interface NetworkClient {

    suspend fun searchTrackByName(trackName: String): TracksSearchResponse

    suspend fun searchTrackById(trackId: Long): TrackDto
}