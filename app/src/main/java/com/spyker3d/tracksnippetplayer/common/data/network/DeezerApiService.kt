package com.spyker3d.tracksnippetplayer.common.data.network

import com.spyker3d.tracksnippetplayer.common.data.network.dto.TrackDto
import com.spyker3d.tracksnippetplayer.common.data.network.dto.TracksSearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DeezerApiService {
    @GET("/search")
    suspend fun searchTrack(@Query("q") text: String): TracksSearchResponse

    @GET("track/{id}")
    suspend fun getTrackById(@Path("id") id: Long): TrackDto
}