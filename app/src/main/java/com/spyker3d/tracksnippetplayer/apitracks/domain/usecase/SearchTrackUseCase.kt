package com.spyker3d.tracksnippetplayer.apitracks.domain.usecase

import android.util.Log
import com.spyker3d.tracksnippetplayer.apitracks.domain.model.Track
import com.spyker3d.tracksnippetplayer.apitracks.domain.repository.TrackSearchRepository
import javax.inject.Inject

class SearchTrackUseCase @Inject constructor(private val trackSearchRepository: TrackSearchRepository) {

    suspend fun searchTrack(trackName: String): List<Track> {
        val response = trackSearchRepository.searchTrack(trackName)
        Log.e("TEST", "track list in usecase: ${response}")
        return response
    }
}