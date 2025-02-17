package com.spyker3d.tracksnippetplayer.search.presentation

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spyker3d.tracksnippetplayer.R
import com.spyker3d.tracksnippetplayer.audioplayer.presentation.AudioPlayerService
import com.spyker3d.tracksnippetplayer.audioplayer.presentation.TRACK_LIST
import com.spyker3d.tracksnippetplayer.audioplayer.presentation.TRACK_NAME
import com.spyker3d.tracksnippetplayer.search.domain.usecase.SearchTrackUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.ArrayList
import javax.inject.Inject


@HiltViewModel
class SearchTrackViewModel @Inject constructor(
    private val searchTrackUseCase: SearchTrackUseCase,
    @ApplicationContext private val context: Context,
) : ViewModel() {
    var searchTrackState by mutableStateOf<SearchState>(SearchState.Empty)
        private set

    private val _showToast = MutableSharedFlow<Int>()
    val showToast = _showToast.asSharedFlow()

    private var searchJob: Job? = null
    private var latestSearchText = ""

    init {
        if (searchTrackState is SearchState.Content) {
            val trackList = (searchTrackState as SearchState.Content).trackList
            val intent = Intent(context, AudioPlayerService::class.java).apply {
                action = AudioPlayerService.ACTION_PREPARE_PLAYLIST
                putParcelableArrayListExtra(TRACK_LIST, ArrayList(trackList))
            }
        }
    }

    fun searchTrack(trackName: String) {
        if (trackName.isEmpty()) {
            latestSearchText = trackName
            searchTrackState = SearchState.Empty
            return
        }

        if (trackName == latestSearchText) return

        searchJob?.cancel()
        latestSearchText = trackName

        searchJob = viewModelScope.launch {
            delay(INPUT_TRACK_SEARCH_DELAY)
            searchTrackState = SearchState.Loading
            try {
                val response = searchTrackUseCase.searchTrack(trackName)
                if (response.isNotEmpty()) {
                    searchTrackState = SearchState.Content(response)
                    val intent = Intent(context, AudioPlayerService::class.java).apply {
                        action = AudioPlayerService.ACTION_PREPARE_PLAYLIST
                        putParcelableArrayListExtra(TRACK_LIST, ArrayList(response))
                    }
                    ContextCompat.startForegroundService(context, intent)
                } else {
                    searchTrackState = SearchState.Empty
                    _showToast.emit(R.string.error_nothing_found)
                }
            } catch (e: Exception) {
                when (e) {
                    is IOException -> {
                        searchTrackState = SearchState.Error
                        _showToast.emit(R.string.error_connection)
                    }

                    else -> {
                        searchTrackState = SearchState.Error
                        _showToast.emit(R.string.something_wrong)
                    }
                }
            }
        }
    }

    companion object {
        const val INPUT_TRACK_SEARCH_DELAY = 2000L
    }
}