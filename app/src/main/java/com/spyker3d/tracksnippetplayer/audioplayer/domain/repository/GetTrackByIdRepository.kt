package com.spyker3d.tracksnippetplayer.audioplayer.domain.repository

import com.spyker3d.tracksnippetplayer.common.domain.model.Track

interface GetTrackByIdRepository {
    suspend fun getTrackById(trackId: Long): Track
}