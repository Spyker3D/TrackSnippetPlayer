package com.spyker3d.tracksnippetplayer.common.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Track(
    val id: Long,
    val name: String,
    val artistName: String,
    val albumName: String,
    val albumImageSmall: String,
    val albumImageMedium: String,
    val albumImageBig: String,
    val link: String,
    val duration: String,
    val audioPreview: String,
    val image: String,
    val uriDownload: String = "",
    val fileNameLocal: String = ""
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Track) return false
        if (this.id != (other as Track).id) return false
        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
