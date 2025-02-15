package com.spyker3d.tracksnippetplayer.downloadedtracks.domain.repository

import android.content.Context
import com.spyker3d.tracksnippetplayer.apitracks.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface TracksDownloadsRepository {
    suspend fun insertTrack(track: Track, context: Context)

    fun getAllDownloadedTracks(): Flow<List<Track>>

    suspend fun getAllDownloadedTracksId(): List<Int>

    suspend fun getTrackById(trackId: Int): Track

    suspend fun deleteTrackById(trackId: Int, context: Context, fileName: String)
}