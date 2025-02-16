package com.spyker3d.tracksnippetplayer.common.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks_downloads_table")
class TrackEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "local_id")
    val localId: Int = 0,
    @ColumnInfo(name = "remote_id")
    val remoteId: Long,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "artist_name")
    val artistName: String,
    @ColumnInfo(name = "album_name")
    val albumName: String,
    @ColumnInfo(name = "album_image_small")
    val albumImageSmall: String,
    @ColumnInfo(name = "album_image_medium")
    val albumImageMedium: String,
    @ColumnInfo(name = "album_image_big")
    val albumImageBig: String,
    @ColumnInfo(name = "link_on_deezer")
    val link: String,
    @ColumnInfo(name = "duration")
    val duration: String,
    @ColumnInfo(name = "audio_preview")
    val audioPreview: String,
    @ColumnInfo(name = "image")
    val image: String,
    @ColumnInfo(name = "uri_download")
    val uriDownload: String,
    @ColumnInfo(name = "time_added")
    val timeAdded: Long,
    @ColumnInfo(name = "file_name")
    val fileNameLocal: String
)