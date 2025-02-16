package com.spyker3d.tracksnippetplayer.search.data.repository

import android.util.Log
import com.spyker3d.tracksnippetplayer.common.data.network.mapper.TrackMapper.mapToDomain
import com.spyker3d.tracksnippetplayer.common.data.network.NetworkClient
import com.spyker3d.tracksnippetplayer.common.domain.model.Track
import com.spyker3d.tracksnippetplayer.search.domain.repository.TrackSearchRepository
import javax.inject.Inject

class TrackSearchRepositoryImpl @Inject constructor(private val networkClient: NetworkClient) :
    TrackSearchRepository {
    override suspend fun searchTrack(request: String): List<Track> {
        Log.e("TEST", "$request")
        Log.e("TEST", "${networkClient.searchTrackByName(request).results}")
        return networkClient.searchTrackByName(request).results.map { it.mapToDomain() }
    }
}