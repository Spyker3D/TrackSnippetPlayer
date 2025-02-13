package com.spyker3d.tracksnippetplayer.apitracks.data.dto

import com.google.gson.annotations.SerializedName

class TracksSearchResponse(
    @SerializedName("data")
    val results: List<TrackDto>
)
