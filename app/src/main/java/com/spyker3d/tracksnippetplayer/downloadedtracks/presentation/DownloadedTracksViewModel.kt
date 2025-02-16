package com.spyker3d.tracksnippetplayer.downloadedtracks.presentation

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spyker3d.tracksnippetplayer.audioplayer.presentation.AudioPlayerService
import com.spyker3d.tracksnippetplayer.audioplayer.presentation.TRACK_LIST
import com.spyker3d.tracksnippetplayer.downloadedtracks.domain.usecase.DownloadedTrackListUseCase
import com.spyker3d.tracksnippetplayer.search.presentation.SearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.util.ArrayList
import javax.inject.Inject

@HiltViewModel
class DownloadedTracksViewModel @Inject constructor(
    downloadedTrackListUseCase: DownloadedTrackListUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    var downloadedTracksList = downloadedTrackListUseCase.getAllDownloadedTracks()
        private set

    init {
        viewModelScope.launch {
            downloadedTracksList.collect { list ->
                val trackList = list
                if (trackList.isNotEmpty()) {
                    val intent = Intent(context, AudioPlayerService::class.java).apply {
                        action = AudioPlayerService.ACTION_PREPARE_PLAYLIST
                        putParcelableArrayListExtra(TRACK_LIST, ArrayList(trackList))
                    }
                    ContextCompat.startForegroundService(context, intent)
                }
            }
        }
    }
}