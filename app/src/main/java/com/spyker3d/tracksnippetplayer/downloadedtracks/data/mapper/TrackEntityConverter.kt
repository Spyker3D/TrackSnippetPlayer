package com.spyker3d.tracksnippetplayer.downloadedtracks.data.mapper

import com.spyker3d.tracksnippetplayer.apitracks.domain.model.Track
import com.spyker3d.tracksnippetplayer.downloadedtracks.data.db.entity.TrackEntity


object TrackEntityConverter {
    fun Track.mapToDb(uriDownload: String, timeAdded: Long, fileName: String): TrackEntity {
        return TrackEntity(
            remoteId = id,
            name = name,
            artistName = artistName,
            albumName = albumName,
            albumImageSmall = albumImageSmall,
            albumImageMedium = albumImageMedium,
            albumImageBig = albumImageBig,
            link = link,
            duration = duration,
            audioPreview = audioPreview,
            image = image,
            uriDownload = uriDownload,
            timeAdded = timeAdded,
            fileNameLocal = fileName
        )
    }

    fun TrackEntity.mapToDomain(): Track {
        return Track(
            id = remoteId,
            name = name,
            artistName = artistName,
            albumName = albumName,
            albumImageSmall = albumImageSmall,
            albumImageMedium = albumImageMedium,
            albumImageBig = albumImageBig,
            link = link,
            duration = duration,
            audioPreview = audioPreview,
            image = image,
            uriDownload = uriDownload,
            fileNameLocal = fileNameLocal
        )
    }
}