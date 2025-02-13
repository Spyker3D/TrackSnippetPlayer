package com.spyker3d.tracksnippetplayer.apitracks.data.network

import com.spyker3d.tracksnippetplayer.apitracks.data.dto.TracksSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface DeezerApiService {
    @GET("/search")
    suspend fun searchTrack(@Query("q") text: String): TracksSearchResponse
}