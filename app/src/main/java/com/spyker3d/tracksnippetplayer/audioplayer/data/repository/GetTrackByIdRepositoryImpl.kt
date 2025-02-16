package com.spyker3d.tracksnippetplayer.audioplayer.data.repository

import com.spyker3d.tracksnippetplayer.audioplayer.domain.repository.GetTrackByIdRepository
import com.spyker3d.tracksnippetplayer.common.data.network.mapper.TrackMapper.mapToDomain
import com.spyker3d.tracksnippetplayer.common.data.network.NetworkClient
import com.spyker3d.tracksnippetplayer.common.domain.model.Track
import javax.inject.Inject

class GetTrackByIdRepositoryImpl @Inject constructor(private val networkClient: NetworkClient) : GetTrackByIdRepository {
    override suspend fun getTrackById(trackId: Int): Track {
        return networkClient.searchTrackById(trackId).mapToDomain()
    }
}