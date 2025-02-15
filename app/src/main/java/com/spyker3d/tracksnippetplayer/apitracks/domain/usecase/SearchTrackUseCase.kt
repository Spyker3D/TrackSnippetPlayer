package com.spyker3d.tracksnippetplayer.apitracks.domain.usecase

import com.spyker3d.tracksnippetplayer.apitracks.domain.model.Track
import com.spyker3d.tracksnippetplayer.apitracks.domain.repository.TrackSearchRepository
import javax.inject.Inject

class SearchTrackUseCase @Inject constructor(private val trackSearchRepository: TrackSearchRepository) {

    suspend fun searchTrack(trackName: String): List<Track> =
        trackSearchRepository.searchTrack(trackName)

    suspend fun getTrackById(trackId: Int): Track = trackSearchRepository.getTrackById(trackId)
}