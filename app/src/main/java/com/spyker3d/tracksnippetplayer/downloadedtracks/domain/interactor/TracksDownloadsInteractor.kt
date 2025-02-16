package com.spyker3d.tracksnippetplayer.downloadedtracks.domain.interactor

import android.content.Context
import com.spyker3d.tracksnippetplayer.apitracks.domain.model.Track
import com.spyker3d.tracksnippetplayer.downloadedtracks.domain.repository.TracksDownloadsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TracksDownloadsInteractor @Inject constructor(
    private val tracksDownloadsRepository: TracksDownloadsRepository
) {
    suspend fun insertTrack(track: Track, context: Context) {
        tracksDownloadsRepository.insertTrack(track = track, context = context)
    }

    fun getAllDownloadedTracks(): Flow<List<Track>> =
        tracksDownloadsRepository.getAllDownloadedTracks()

    suspend fun getAllDownloadedTracksId(): List<Int> =
        tracksDownloadsRepository.getAllDownloadedTracksId()

    suspend fun getTrackById(trackId: Int): Track = tracksDownloadsRepository.getTrackById(trackId)

    suspend fun deleteTrackById(trackId: Int, context: Context, fileName: String) {
        tracksDownloadsRepository.deleteTrackById(
            trackId = trackId,
            context = context,
            fileName = fileName
        )
    }
}