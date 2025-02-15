package com.spyker3d.tracksnippetplayer.downloadedtracks.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.spyker3d.tracksnippetplayer.downloadedtracks.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TracksDownloadsDao {
    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track: TrackEntity)

    @Delete(entity = TrackEntity::class)
    suspend fun deleteEntity(track: TrackEntity)

    @Query("SELECT * FROM tracks_downloads_table ORDER BY time_added DESC")
    fun getAllDownloadedTracks(): Flow<List<TrackEntity>>

    @Query("SELECT remote_id FROM tracks_downloads_table")
    suspend fun getAllDownloadedTracksId(): List<Int>

    @Transaction
    @Query("SELECT * FROM tracks_downloads_table WHERE remote_id = :trackId")
    suspend fun getTrackById(trackId: Int): TrackEntity

    @Transaction
    @Query("DELETE FROM tracks_downloads_table WHERE remote_id = :trackId")
    suspend fun deleteTrackById(trackId: Int)
}