package com.spyker3d.tracksnippetplayer.apitracks.data.dto

import com.google.gson.annotations.SerializedName

class TrackDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val name: String,
    @SerializedName("link")
    val link: String,
    @SerializedName("duration")
    val durationInSec: Int,
    @SerializedName("preview")
    val audioPreview: String,
    @SerializedName("md5_image")
    val image: String,
    @SerializedName("artist")
    val artist: ArtistDto,
    @SerializedName("album")
    val album: AlbumDto,
)

class AlbumDto(
    @SerializedName("id")
    val albumId: Int,
    @SerializedName("title")
    val albumTitle: String,
    @SerializedName("cover")
    val albumCover: String,
    @SerializedName("cover_small")
    val albumCoverSmall: String,
    @SerializedName("cover_medium")
    val albumCoverMedium: String,
    @SerializedName("cover_big")
    val albumCoverBig: String,
    @SerializedName("cover_xl")
    val albumCoverXl: String,
    @SerializedName("genres")
    val genresList: List<Genre>?,
    @SerializedName("label")
    val albumLabel: String?,
    @SerializedName("release_date")
    val albumReleaseDate: String?,
)

class Genre(
    @SerializedName("id")
    val genreId: Int,
    @SerializedName("name")
    val genreName: String
)

class ArtistDto(
    @SerializedName("id")
    val artistId: Int,
    @SerializedName("name")
    val artistName: String,
    @SerializedName("picture")
    val artistPicture: String,
    @SerializedName("picture_small")
    val artistPictureSmall: String,
    @SerializedName("picture_medium")
    val artistPictureMedium: String,
    @SerializedName("picture_big")
    val artistPictureBig: String,
)
