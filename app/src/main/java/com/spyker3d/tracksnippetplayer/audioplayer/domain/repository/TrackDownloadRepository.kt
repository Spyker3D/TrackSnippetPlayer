package com.spyker3d.tracksnippetplayer.audioplayer.domain.repository

import android.content.Context
import com.spyker3d.tracksnippetplayer.common.domain.model.Track

interface TrackDownloadRepository {
    suspend fun insertTrack(track: Track, context: Context)

    suspend fun getAllDownloadedTracksId(): List<Int>

    suspend fun getTrackById(trackId: Int): Track

    suspend fun deleteTrackById(trackId: Int, context: Context, fileName: String)
}