package com.spyker3d.tracksnippetplayer.di

import com.spyker3d.tracksnippetplayer.common.data.network.NetworkClient
import com.spyker3d.tracksnippetplayer.common.data.network.RetrofitNetworkClient
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NetworkClientModule {
    @Binds
    @Singleton
     fun bindNetworkClient(retrofitNetworkClient: RetrofitNetworkClient) : NetworkClient
}