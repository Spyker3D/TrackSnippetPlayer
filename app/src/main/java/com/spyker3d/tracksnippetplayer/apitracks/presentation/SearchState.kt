package com.spyker3d.tracksnippetplayer.apitracks.presentation

import com.spyker3d.tracksnippetplayer.apitracks.domain.model.Track

sealed interface SearchState {
    object Loading : SearchState
    data class Content(val trackList: List<Track>) : SearchState
    data class ConnectionError(val errorMessage: String) : SearchState
    data class OtherError(val errorMessage: String) : SearchState
    object Empty : SearchState
}