package com.spyker3d.tracksnippetplayer.di

import com.spyker3d.tracksnippetplayer.apitracks.data.repository.TrackSearchRepositoryImpl
import com.spyker3d.tracksnippetplayer.apitracks.domain.repository.TrackSearchRepository
import com.spyker3d.tracksnippetplayer.downloadedtracks.data.repository.TracksDownloadsRepositoryImpl
import com.spyker3d.tracksnippetplayer.downloadedtracks.domain.repository.TracksDownloadsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    @Singleton
    fun bindSearchTrackRepository(searchTrackSearchRepositoryImpl: TrackSearchRepositoryImpl): TrackSearchRepository

    @Binds
    @Singleton
    fun bindTracksDownloadsRepository(tracksDownloadsRepositoryImpl: TracksDownloadsRepositoryImpl) : TracksDownloadsRepository

}