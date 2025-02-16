package com.spyker3d.tracksnippetplayer.downloadedtracks.presentation

import androidx.lifecycle.ViewModel
import com.spyker3d.tracksnippetplayer.downloadedtracks.domain.usecase.DownloadedTrackListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DownloadedTracksViewModel @Inject constructor(
    downloadedTrackListUseCase: DownloadedTrackListUseCase
) : ViewModel() {

    var downloadedTracksList = downloadedTrackListUseCase.getAllDownloadedTracks()
        private set

}