package com.spyker3d.tracksnippetplayer.audioplayer.presentation

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spyker3d.tracksnippetplayer.R
import com.spyker3d.tracksnippetplayer.audioplayer.domain.interactor.GetTrackByIdUseCase
import com.spyker3d.tracksnippetplayer.audioplayer.domain.interactor.TrackDownloadInteractor
import com.spyker3d.tracksnippetplayer.common.domain.model.Track
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
    private val getTrackByIdUseCase: GetTrackByIdUseCase,
    private val trackDownloadInteractor: TrackDownloadInteractor,
    state: SavedStateHandle,
) : ViewModel() {

    private val trackId: Long = state.get<Long>("trackId")!!
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
                    val response = getTrackByIdUseCase.getTrackById(trackId)
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
                trackState = TrackState.Loading
                try {
                    val localTrackInfo = trackDownloadInteractor.getTrackById(trackId)
                    trackState = TrackState.Success(localTrackInfo)
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
        }
    }

    private fun sendCommandToService(
        action: String,
        vararg extraStrings: Pair<String, String>?,
        extraLong: Pair<String, Long>? = null,
        extraBoolean: Pair<String, Boolean>? = null
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

    fun prepareTrack(
        trackUrl: String,
        trackName: String,
        artistName: String,
        trackId: Long,
        isDownloadsScreen: Boolean
    ) {
        sendCommandToService(
            AudioPlayerService.ACTION_PREPARE,
            TRACK_URL to trackUrl,
            TRACK_NAME to trackName,
            ARTIST_NAME to artistName,
            extraLong = TRACK_ID to trackId,
            extraBoolean = IS_DOWNLOADS_SCREEN to isDownloadsScreen // для определения того, открыт экран аудиоплеера с экрана поиска или с экрана загрузок
                                                                    // (отличается UI и функционал у экрана плеера в зависимости от того, откуда пользователь пришел)
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
            extraLong = SEEK_POSITION to position
        )
    }

    fun downloadTrack(track: Track) {
        viewModelScope.launch {
            val listOfDownloadedTracks = trackDownloadInteractor.getAllDownloadedTracksId()
            if (!listOfDownloadedTracks.contains(track.id)) {
                try {
                    trackDownloadInteractor.insertTrack(track = track, context = context)
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
            val listOfDownloadedTracks = trackDownloadInteractor.getAllDownloadedTracksId()
            if (listOfDownloadedTracks.contains(track.id)) {
                try {
                    trackDownloadInteractor.deleteTrackById(
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