package com.spyker3d.tracksnippetplayer.apitracks.data.mapper

import com.spyker3d.tracksnippetplayer.apitracks.data.dto.TrackDto
import com.spyker3d.tracksnippetplayer.apitracks.domain.model.Track
import java.text.SimpleDateFormat
import java.util.Locale

object TrackMapper {
    private val trackTimeFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    private val releaseDateFormatDetailed by lazy {
        SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss'Z'",
            Locale.getDefault()
        )
    }
    private val releaseYearFormat by lazy { SimpleDateFormat("yyyy", Locale.getDefault()) }

    private fun trackTimeMillisFormat(trackTimeMillis: Int): String {
        return trackTimeFormat.format(trackTimeMillis * 1_000)
    }

    private fun formatReleaseDateToYear(releaseDate: String?): String? {
        return if (releaseDate != null) {
            releaseDateFormatDetailed.parse(releaseDate)?.let { releaseYearFormat.format(it) }
        } else null
    }

    private fun makeLargePreview(previewUrl: String?): String? {
        return previewUrl?.replaceAfterLast('/', "512x512bb.jpg")
    }

    private fun formatYearToReleaseDate(releaseYear: String?): String? {
        return if (releaseYear != null) {
            releaseYearFormat.parse(releaseYear)?.let { releaseDateFormatDetailed.format(it) }
        } else null
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
            image = image
        )
    }
}