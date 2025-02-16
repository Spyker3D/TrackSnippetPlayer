package com.spyker3d.tracksnippetplayer.common.data.network.mapper

import com.spyker3d.tracksnippetplayer.common.data.network.dto.TrackDto
import com.spyker3d.tracksnippetplayer.common.domain.model.Track
import java.text.SimpleDateFormat
import java.util.Locale

object TrackMapper {
    private val trackTimeFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    private fun trackTimeMillisFormat(trackTimeMillis: Int): String {
        return trackTimeFormat.format(trackTimeMillis * 1_000)
    }

    fun TrackDto.mapToDomain(): Track {
        return Track(
            id = id,
            name = name,
            artistName = artist.artistName,
            albumName = album.albumTitle,
            link = link,
            duration = trackTimeMillisFormat(durationInSec),
            audioPreview = audioPreview,
            image = image,
            albumImageSmall = album.albumCoverSmall,
            albumImageMedium = album.albumCoverMedium,
            albumImageBig = album.albumCoverBig
        )
    }
}