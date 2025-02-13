package com.spyker3d.tracksnippetplayer.apitracks.data.network

import com.spyker3d.tracksnippetplayer.apitracks.data.dto.TracksSearchResponse

interface NetworkClient {

    suspend fun doRequest(dto: String): TracksSearchResponse
}