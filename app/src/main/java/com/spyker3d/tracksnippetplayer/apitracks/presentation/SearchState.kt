package com.spyker3d.tracksnippetplayer.apitracks.presentation

import com.spyker3d.tracksnippetplayer.apitracks.domain.model.Track

sealed interface SearchState {
    object Loading : SearchState
    data class Content(val trackList: List<Track>) : SearchState
    object Empty : SearchState
    object Error: SearchState
}