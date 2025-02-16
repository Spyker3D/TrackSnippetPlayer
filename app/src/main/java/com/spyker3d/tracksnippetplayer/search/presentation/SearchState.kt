package com.spyker3d.tracksnippetplayer.search.presentation

import com.spyker3d.tracksnippetplayer.common.domain.model.Track

sealed interface SearchState {
    object Loading : SearchState
    data class Content(val trackList: List<Track>) : SearchState
    object Empty : SearchState
    object Error: SearchState
}