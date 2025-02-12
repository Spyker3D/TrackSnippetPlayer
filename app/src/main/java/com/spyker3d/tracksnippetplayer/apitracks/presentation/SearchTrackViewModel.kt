package com.spyker3d.tracksnippetplayer.apitracks.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spyker3d.tracksnippetplayer.R
import com.spyker3d.tracksnippetplayer.apitracks.domain.usecase.SearchTrackUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class SearchTrackViewModel @Inject constructor(
    private val searchTrackUseCase: SearchTrackUseCase
) : ViewModel() {
    var searchTrackState by mutableStateOf<SearchState>(SearchState.Empty)
        private set

    private val _showToast = MutableSharedFlow<Int>()
    val showToast = _showToast.asSharedFlow()

    private var searchJob: Job? = null
    private var latestSearchText = ""

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
                Log.e("TEST", "search track in ViewModel")
                val response = searchTrackUseCase.searchTrack(trackName)
                Log.e("TEST", "response in viewmodel: $response")
                if (response.isNotEmpty()) {
                    searchTrackState = SearchState.Content(response)
                } else {
                    searchTrackState = SearchState.Empty
                    _showToast.emit(R.string.error_nothing_found)
                }
            } catch (e: Exception) {
                when(e) {
                    is IOException -> {
                        searchTrackState = SearchState.ConnectionError(e.message ?: "")
                        _showToast.emit(R.string.error_connection)
                    }
                    else -> {
                        searchTrackState = SearchState.OtherError(e.message ?: "")
                        Log.e("TEST", "Something went wrong: ${e.message}", e)
                        _showToast.emit(R.string.something_wrong)
                    }
                }
            }
        }
    }

    companion object {
        const val INPUT_TRACK_SEARCH_DELAY = 1000L
    }
}