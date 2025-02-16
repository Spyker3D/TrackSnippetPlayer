package com.spyker3d.tracksnippetplayer.downloadedtracks.data.repository

import com.spyker3d.tracksnippetplayer.common.data.db.AppDatabase
import com.spyker3d.tracksnippetplayer.common.data.db.mapper.TrackEntityConverter.mapToDomain
import com.spyker3d.tracksnippetplayer.downloadedtracks.domain.repository.DownloadedTrackListRepository
import com.spyker3d.tracksnippetplayer.common.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DownloadedTrackListRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase
) : DownloadedTrackListRepository {
    override fun getAllDownloadedTracks(): Flow<List<Track>> {
        return appDatabase.trackDownloadsDao().getAllDownloadedTracks().map { it ->
            it.map { trackEntity ->
                trackEntity.mapToDomain()
            }
        }
    }
}