package com.spyker3d.tracksnippetplayer.audioplayer.data.repository

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.spyker3d.tracksnippetplayer.audioplayer.domain.repository.TrackDownloadRepository
import com.spyker3d.tracksnippetplayer.common.data.db.AppDatabase
import com.spyker3d.tracksnippetplayer.common.data.db.mapper.TrackEntityConverter.mapToDb
import com.spyker3d.tracksnippetplayer.common.data.db.mapper.TrackEntityConverter.mapToDomain
import com.spyker3d.tracksnippetplayer.common.domain.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.IOException
import javax.inject.Inject

class TrackDownloadRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase
) : TrackDownloadRepository {
    override suspend fun insertTrack(track: Track, context: Context) {
        val trackUrl = track.audioPreview
        val fileName = "${track.name}_${track.id}.mp3"
        coroutineScope {
            val downloadDeferred =
                async {
                    downloadTrackToLocal(
                        context = context,
                        trackUrl = trackUrl,
                        fileName = fileName
                    )
                }
            downloadDeferred.await().let {
                val timeAdded = System.currentTimeMillis()
                appDatabase.trackDownloadsDao().insertTrack(
                    track.mapToDb(
                        uriDownload = it.toString(),
                        timeAdded = timeAdded,
                        fileName = fileName
                    )
                )
            }
        }
    }

    override suspend fun getAllDownloadedTracksId(): List<Long> {
        return appDatabase.trackDownloadsDao().getAllDownloadedTracksId()
    }

    override suspend fun getTrackById(trackId: Long): Track {
        val localTrack = appDatabase.trackDownloadsDao().getTrackById(trackId)
        return localTrack.mapToDomain()
    }

    override suspend fun deleteTrackById(trackId: Long, context: Context, fileName: String) {
        appDatabase.trackDownloadsDao().deleteTrackById(trackId)
        coroutineScope {
            launch {
                deleteDownloadedTrack(context = context, fileName = fileName)
            }
        }
    }

    private suspend fun downloadTrackToLocal(
        context: Context,
        trackUrl: String,
        fileName: String
    ): Uri? = withContext(Dispatchers.IO) {
        try {
            val client = OkHttpClient()
            val request = Request.Builder().url(trackUrl).build()
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected error: $response")
                val musicDir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
                    ?: throw IOException("External files directory is not available")
                val appFolder = File(musicDir, "TrackSnippetPlayer")
                if (!appFolder.exists()) {
                    appFolder.mkdirs()
                }

                val file = File(appFolder, fileName)
                file.outputStream().use { outputStream ->
                    response.body?.byteStream()?.copyTo(outputStream)
                }

                return@withContext FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    file
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext null
        }
    }

    private suspend fun deleteDownloadedTrack(
        context: Context,
        fileName: String
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val musicDir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
                ?: throw IOException("External files directory is not available")
            val appFolder = File(musicDir, "TrackSnippetPlayer")
            if (!appFolder.exists()) {
                return@withContext false
            }
            val file = File(appFolder, fileName)
            if (file.exists()) {
                return@withContext file.delete()
            } else {
                return@withContext false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext false
        }
    }
}