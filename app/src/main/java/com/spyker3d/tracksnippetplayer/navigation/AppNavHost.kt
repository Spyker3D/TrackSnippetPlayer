package com.spyker3d.tracksnippetplayer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.spyker3d.tracksnippetplayer.apitracks.presentation.SearchTrackScreen
import com.spyker3d.tracksnippetplayer.audioplayer.presentation.AudioPlayerScreen
import com.spyker3d.tracksnippetplayer.downloadedtracks.presentation.DownloadedTracksScreen
import kotlinx.serialization.Serializable

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = ApiTracks
    ) {
        composable<ApiTracks> {
            SearchTrackScreen(onNavigateToAudioPlayer = {
                navController.navigate(
                    route = AudioPlayer
                )
            })
        }

        composable<DownloadedTracks> {
            DownloadedTracksScreen(onNavigateToAudioPlayer = {
                navController.navigate(
                    route = AudioPlayer
                )
            })
        }

        composable<AudioPlayer> {
            val args = it.toRoute<AudioPlayer>()
            AudioPlayerScreen(
                trackId = args.id,
                onBackPressed = { navController.popBackStack() }
                )
        }
    }

}

@Serializable
object ApiTracks

@Serializable
object DownloadedTracks

@Serializable
data class AudioPlayer(
    val id: String,
)