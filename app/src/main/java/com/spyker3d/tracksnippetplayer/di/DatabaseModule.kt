package com.spyker3d.tracksnippetplayer.di

import android.content.Context
import androidx.room.Room
import com.spyker3d.tracksnippetplayer.common.data.db.AppDatabase
import com.spyker3d.tracksnippetplayer.common.data.db.dao.TracksDownloadsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    fun provideTrackDownloadsDao(database: AppDatabase): TracksDownloadsDao = database.trackDownloadsDao()

    @Singleton
    @Provides
    fun provideDataBase(
        @ApplicationContext context: Context,
    ): AppDatabase =
        Room
            .databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "TrackDownloads.db",
            ).build()
}