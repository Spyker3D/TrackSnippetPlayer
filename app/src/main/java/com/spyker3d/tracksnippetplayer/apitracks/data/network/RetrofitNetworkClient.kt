package com.spyker3d.tracksnippetplayer.apitracks.data.network

import android.util.Log
import com.spyker3d.tracksnippetplayer.apitracks.data.dto.TracksSearchResponse
import javax.inject.Inject

class RetrofitNetworkClient @Inject constructor(
    private val deezerApiService: DeezerApiService
) : NetworkClient {

    override suspend fun doRequest(dto: String): TracksSearchResponse {
        Log.e("TEST", "search track in RetrofitNewtworkClient")
        return deezerApiService.searchTrack(dto)
    }
}