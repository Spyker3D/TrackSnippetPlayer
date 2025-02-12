package com.spyker3d.tracksnippetplayer.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.spyker3d.tracksnippetplayer.R
import com.spyker3d.tracksnippetplayer.apitracks.presentation.SearchTrackScreen
import com.spyker3d.tracksnippetplayer.audioplayer.presentation.AudioPlayerScreen
import com.spyker3d.tracksnippetplayer.downloadedtracks.presentation.DownloadedTracksScreen
import com.spyker3d.tracksnippetplayer.ui.theme.grey
import kotlinx.serialization.Serializable


@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    val items = listOf(
        BottomNavigationItem(
            title = "Search",
            selectedIcon = ImageVector.vectorResource(R.drawable.ic_search_tab_selected),
            unselectedIcon = ImageVector.vectorResource(R.drawable.ic_search_tab_unselected),
            route = ApiTracks
        ),
        BottomNavigationItem(
            title = "Downloads",
            selectedIcon = ImageVector.vectorResource(R.drawable.ic_downloaded_tracks_tab_selected),
            unselectedIcon = ImageVector.vectorResource(R.drawable.ic_downloaded_tracks_tab_unselected),
            route = DownloadedTracks
        )
    )

    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    Scaffold(
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        val strokeWidth = 1.dp.toPx()
                        drawLine(
                            color = grey,
                            start = Offset(0f, 0f),
                            end = Offset(size.width, 0f),
                            strokeWidth = strokeWidth
                        )
                    }
            ) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                ) {
                    items.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = selectedItemIndex == index,
                            onClick = {
                                selectedItemIndex = index
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            label = {
                                Text(
                                    text = item.title,
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily(Font(R.font.roboto_medium))
                                )
                            },
                            icon = {
                                Icon(
                                    imageVector = if (index == selectedItemIndex) {
                                        item.selectedIcon
                                    } else {
                                        item.unselectedIcon
                                    },
                                    contentDescription = item.title
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = Color.Transparent
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = ApiTracks,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<ApiTracks> {
                SearchTrackScreen(onNavigateToAudioPlayer = { id ->
                    navController.navigate(
                        route = AudioPlayer(id = id)
                    )
                })
            }

            composable<DownloadedTracks> {
                DownloadedTracksScreen(onNavigateToAudioPlayer = { id ->
                    navController.navigate(
                        route = AudioPlayer(id = id)
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
}

@Serializable
object ApiTracks

@Serializable
object DownloadedTracks

@Serializable
data class AudioPlayer(
    val id: String,
)