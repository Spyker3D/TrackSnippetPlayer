package com.spyker3d.tracksnippetplayer.apitracks.domain.model

class Track(
    val id: Int,
    val name: String,
    val artistName: String,
    val albumName: String,
    val link: String,
    val duration: String,
    val audioPreview: String,
    val image: String,
) {
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
