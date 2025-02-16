package com.spyker3d.tracksnippetplayer.downloadedtracks.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.spyker3d.tracksnippetplayer.downloadedtracks.data.db.dao.TracksDownloadsDao
import com.spyker3d.tracksnippetplayer.downloadedtracks.data.db.entity.TrackEntity

@Database(
    entities = [TrackEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDownloadsDao(): TracksDownloadsDao
}