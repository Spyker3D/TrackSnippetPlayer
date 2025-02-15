package com.spyker3d.tracksnippetplayer.apitracks.data.repository

import com.spyker3d.tracksnippetplayer.apitracks.data.mapper.TrackMapper.mapToDomain
import com.spyker3d.tracksnippetplayer.apitracks.data.network.NetworkClient
import com.spyker3d.tracksnippetplayer.apitracks.domain.model.Track
import com.spyker3d.tracksnippetplayer.apitracks.domain.repository.TrackSearchRepository
import javax.inject.Inject

class TrackSearchRepositoryImpl @Inject constructor(private val networkClient: NetworkClient) :
    TrackSearchRepository {
    override suspend fun searchTrack(request: String): List<Track> {
        return networkClient.searchTrackByName(request).results.map { it.mapToDomain() }
    }

    override suspend fun getTrackById(trackId: Int): Track {
        return networkClient.searchTrackById(trackId).mapToDomain()
    }
}