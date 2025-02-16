package com.spyker3d.tracksnippetplayer.audioplayer.domain.interactor

import android.content.Context
import com.spyker3d.tracksnippetplayer.audioplayer.domain.repository.TrackDownloadRepository
import com.spyker3d.tracksnippetplayer.common.domain.model.Track
import javax.inject.Inject

class TrackDownloadInteractor @Inject constructor(
    private val trackDownloadRepository: TrackDownloadRepository
) {
    suspend fun insertTrack(track: Track, context: Context) {
        trackDownloadRepository.insertTrack(track = track, context = context)
    }

    suspend fun getAllDownloadedTracksId(): List<Long> =
        trackDownloadRepository.getAllDownloadedTracksId()

    suspend fun getTrackById(trackId: Long): Track = trackDownloadRepository.getTrackById(trackId)

    suspend fun deleteTrackById(trackId: Long, context: Context, fileName: String) {
        trackDownloadRepository.deleteTrackById(
            trackId = trackId,
            context = context,
            fileName = fileName
        )
    }
}