package com.spyker3d.tracksnippetplayer.di

import com.spyker3d.tracksnippetplayer.audioplayer.data.repository.GetTrackByIdRepositoryImpl
import com.spyker3d.tracksnippetplayer.audioplayer.data.repository.TrackDownloadRepositoryImpl
import com.spyker3d.tracksnippetplayer.audioplayer.domain.repository.GetTrackByIdRepository
import com.spyker3d.tracksnippetplayer.audioplayer.domain.repository.TrackDownloadRepository
import com.spyker3d.tracksnippetplayer.search.data.repository.TrackSearchRepositoryImpl
import com.spyker3d.tracksnippetplayer.search.domain.repository.TrackSearchRepository
import com.spyker3d.tracksnippetplayer.downloadedtracks.data.repository.DownloadedTrackListRepositoryImpl
import com.spyker3d.tracksnippetplayer.downloadedtracks.domain.repository.DownloadedTrackListRepository
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
    fun bindDownloadedTrackListRepository(downloadedTrackListRepository: DownloadedTrackListRepositoryImpl) : DownloadedTrackListRepository

    @Binds
    @Singleton
    fun bindTrackDownloadRepository(trackDownloadRepository: TrackDownloadRepositoryImpl) : TrackDownloadRepository

    @Binds
    @Singleton
    fun getTrackByIdRepository(getTrackByIdRepository: GetTrackByIdRepositoryImpl) : GetTrackByIdRepository

}