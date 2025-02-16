package com.spyker3d.tracksnippetplayer.audioplayer.presentation

import com.spyker3d.tracksnippetplayer.common.domain.model.Track

sealed interface TrackState {
    object Idle : TrackState
    object Loading : TrackState
    class Success(val data: Track) : TrackState
    object Error : TrackState
}
