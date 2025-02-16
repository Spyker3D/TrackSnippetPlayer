package com.spyker3d.tracksnippetplayer.search.domain.usecase

import com.spyker3d.tracksnippetplayer.common.domain.model.Track
import com.spyker3d.tracksnippetplayer.search.domain.repository.TrackSearchRepository
import javax.inject.Inject

class SearchTrackUseCase @Inject constructor(private val trackSearchRepository: TrackSearchRepository) {

    suspend fun searchTrack(trackName: String): List<Track> =
        trackSearchRepository.searchTrack(trackName)
}