package com.spyker3d.tracksnippetplayer.audioplayer.domain.repository

interface AudioPlayerRepository {

    fun preparePlayer(trackUrl: String)

    fun startPlayer()

    fun pausePlayer()

    fun rewind()

    fun fastForward()

    fun next()

    fun previous()

}