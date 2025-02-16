package com.spyker3d.tracksnippetplayer.downloadedtracks.presentation

import androidx.lifecycle.ViewModel
import com.spyker3d.tracksnippetplayer.downloadedtracks.domain.interactor.TracksDownloadsInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DownloadedTracksViewModel @Inject constructor(
    tracksDownloadsInteractor: TracksDownloadsInteractor
) : ViewModel() {

    var downloadedTracksList = tracksDownloadsInteractor.getAllDownloadedTracks()
        private set

}