package com.spyker3d.tracksnippetplayer.common.data.network

import com.spyker3d.tracksnippetplayer.common.data.network.dto.TrackDto
import com.spyker3d.tracksnippetplayer.common.data.network.dto.TracksSearchResponse
import javax.inject.Inject

class RetrofitNetworkClient @Inject constructor(
    private val deezerApiService: DeezerApiService
) : NetworkClient {

    override suspend fun searchTrackByName(trackName: String): TracksSearchResponse {
        return deezerApiService.searchTrack(trackName)
    }

    override suspend fun searchTrackById(trackId: Int): TrackDto {
        return deezerApiService.getTrackById(trackId)
    }
}