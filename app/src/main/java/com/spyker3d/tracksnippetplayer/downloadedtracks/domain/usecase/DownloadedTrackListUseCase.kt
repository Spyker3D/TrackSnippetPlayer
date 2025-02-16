package com.spyker3d.tracksnippetplayer.downloadedtracks.domain.usecase

import com.spyker3d.tracksnippetplayer.downloadedtracks.domain.repository.DownloadedTrackListRepository
import com.spyker3d.tracksnippetplayer.common.domain.model.Track
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DownloadedTrackListUseCase @Inject constructor(
    private val downloadedTrackListRepository: DownloadedTrackListRepository
) {
    fun getAllDownloadedTracks(): Flow<List<Track>> =
        downloadedTrackListRepository.getAllDownloadedTracks()
}