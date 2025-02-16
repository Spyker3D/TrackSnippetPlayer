package com.spyker3d.tracksnippetplayer.audioplayer.domain.repository

import android.content.Context
import com.spyker3d.tracksnippetplayer.common.domain.model.Track

interface TrackDownloadRepository {
    suspend fun insertTrack(track: Track, context: Context)

    suspend fun getAllDownloadedTracksId(): List<Long>

    suspend fun getTrackById(trackId: Long): Track

    suspend fun deleteTrackById(trackId: Long, context: Context, fileName: String)
}