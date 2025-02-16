package com.spyker3d.tracksnippetplayer.audioplayer.presentation

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.spyker3d.tracksnippetplayer.R
import com.spyker3d.tracksnippetplayer.apitracks.domain.model.Track
import com.spyker3d.tracksnippetplayer.apitracks.domain.usecase.SearchTrackUseCase
import com.spyker3d.tracksnippetplayer.downloadedtracks.domain.interactor.TracksDownloadsInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AudioPlayerViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val searchTrackUseCase: SearchTrackUseCase,
    private val tracksDownloadsInteractor: TracksDownloadsInteractor,
    state: SavedStateHandle,
) : ViewModel() {

    private val trackId: Int = state.get<Int>("trackId")!!
    private val isDownloadsScreenRoute: Boolean = state.get<Boolean>("isDownloadedScreen")!!

    val playbackState: StateFlow<PlaybackState> = PlaybackStateManager.playbackStateFlow

    var trackState by mutableStateOf<TrackState>(TrackState.Idle)
        private set

    private val _showToast = MutableSharedFlow<Int>()
    val showToast = _showToast.asSharedFlow()


    init {
        if (!isDownloadsScreenRoute) {
            viewModelScope.launch {
                trackState = TrackState.Loading
                try {
                    val response = searchTrackUseCase.getTrackById(trackId)
                    trackState = TrackState.Success(response)
                } catch (e: Exception) {
                    when (e) {
                        is IOException -> {
                            trackState = TrackState.Error
                            _showToast.emit(R.string.error_connection)
                        }

                        else -> {
                            trackState = TrackState.Error
                            _showToast.emit(R.string.something_wrong)
                        }
                    }
                }
            }
        } else {
            viewModelScope.launch {
                Log.e("TEST", "viewmodel is downloadedscreen TRUE")
                trackState = TrackState.Loading
                try {
                    val localTrackInfo = tracksDownloadsInteractor.getTrackById(trackId)
                    trackState = TrackState.Success(localTrackInfo)
                    Log.e("TEST", "localtrack in view model: ${localTrackInfo.name}")
                } catch (e: Exception) {
                    when (e) {
                        is IOException -> {
                            trackState = TrackState.Error
                            _showToast.emit(R.string.error_connection)
                        }

                        else -> {
                            Log.e("TEST", "${e.message} and $e")
                            trackState = TrackState.Error
                            _showToast.emit(R.string.something_wrong)
                        }
                    }
                }
            }
        }
    }

    private fun sendCommandToService(
        action: String,
        vararg extraStrings: Pair<String, String>?,
        extraLong: Pair<String, Long>? = null,
    ) {
        val intent = Intent(context, AudioPlayerService::class.java).apply {
            this.action = action
            extraStrings.forEach { pair ->
                putExtra(pair?.first, pair?.second)
            }
            extraLong?.let { putExtra(it.first, it.second) }
        }
        ContextCompat.startForegroundService(context, intent)
    }

    fun prepareTrack(trackUrl: String, trackName: String, artistName: String) {
        sendCommandToService(
            AudioPlayerService.ACTION_PREPARE,
            "TRACK_URL" to trackUrl,
            "TRACK_NAME" to trackName,
            "ARTIST_NAME" to artistName
        )
    }

    fun playPause() {
        val action = if (playbackState.value.isPlaying) {
            AudioPlayerService.ACTION_PAUSE
        } else {
            AudioPlayerService.ACTION_PLAY
        }
        sendCommandToService(action)
    }

    fun rewind() {
        sendCommandToService(AudioPlayerService.ACTION_REWIND)
    }

    fun fastForward() {
        sendCommandToService(AudioPlayerService.ACTION_FAST_FORWARD)
    }

    fun seekTo(position: Long) {
        sendCommandToService(
            AudioPlayerService.ACTION_SEEK_TO,
            extraLong = "SEEK_POSITION" to position
        )
    }

    fun downloadTrack(track: Track) {
        viewModelScope.launch {
            val listOfDownloadedTracks = tracksDownloadsInteractor.getAllDownloadedTracksId()
            if (!listOfDownloadedTracks.contains(track.id)) {
                try {
                    tracksDownloadsInteractor.insertTrack(track = track, context = context)
                    _showToast.emit(R.string.track_downloaded)
                } catch (e: Exception) {
                    _showToast.emit(R.string.something_wrong)
                }
            } else {
                _showToast.emit(R.string.track_download_error)
            }
        }
    }

    fun deleteTrackFromDownloads(track: Track) {
        viewModelScope.launch {
            val listOfDownloadedTracks = tracksDownloadsInteractor.getAllDownloadedTracksId()
            if (listOfDownloadedTracks.contains(track.id)) {
                try {
                    tracksDownloadsInteractor.deleteTrackById(
                        trackId = track.id,
                        context = context,
                        fileName = track.fileNameLocal
                    )
                    _showToast.emit(R.string.track_was_deleted)
                } catch (e: Exception) {
                    _showToast.emit(R.string.something_wrong)
                }
            } else {
                _showToast.emit(R.string.track_deletion_error)
            }
        }
    }
}