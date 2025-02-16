package com.spyker3d.tracksnippetplayer.common.data.network.dto

import com.google.gson.annotations.SerializedName

class TracksSearchResponse(
    @SerializedName("data")
    val results: List<TrackDto>
)
