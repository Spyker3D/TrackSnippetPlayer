package com.spyker3d.tracksnippetplayer.downloadedtracks.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spyker3d.tracksnippetplayer.apitracks.domain.model.Track
import com.spyker3d.tracksnippetplayer.apitracks.presentation.TrackList
import com.spyker3d.tracksnippetplayer.ui.theme.TrackSnippetPlayerTheme

@Composable
fun DownloadedTracksScreen(
    modifier: Modifier = Modifier,
    onNavigateToAudioPlayer: (trackId: Int, trackPreviewUrl: String) -> Unit,
    downloadedTracksListState: List<Track>
) {
    Box(
        modifier = modifier,
    ) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.surface,
            topBar = {
                TopBar()
            },
            content = { paddingValues ->
                Box(
                    modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(
                            top = paddingValues.calculateTopPadding(),
                            bottom = paddingValues.calculateBottomPadding(),
                        ),
                ) {
                    if (downloadedTracksListState.isNotEmpty()) {
                        TrackList(
                            listOfTracks = downloadedTracksListState,
                            isDeleteIconVisible = false,
                            onDeleteItemListener = {},
                            onClickListener = { trackId, trackAudioPreview ->
                                onNavigateToAudioPlayer(trackId, trackAudioPreview)
                            }
                        )
                    }

                }

            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        modifier =
        modifier
            .padding(top = 20.dp, bottom = 20.dp, start = 10.dp)
            .wrapContentHeight(),
        title = {
            Text(
                modifier = Modifier.padding(horizontal = 0.dp),
                text = "Скачанные треки",
                fontSize = 40.sp,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium,
                lineHeight = 44.sp,
            )
        },
        colors =
        TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    )
}

@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
@Composable
fun CurrentPurchasesListScreenScaffoldPreview() {
    TrackSnippetPlayerTheme {
        DownloadedTracksScreen(
            onNavigateToAudioPlayer = { int, string -> Unit },
            downloadedTracksListState = listOf(
                Track(
                    id = 123,
                    name = "TestTrack",
                    artistName = "TestArtist",
                    albumName = "TestCollection",
                    link = "test.ru",
                    duration = "4:33",
                    audioPreview = "test3.ru",
                    image = "test4.ru",
                    albumImageSmall = "small.ru",
                    albumImageMedium = "medium.ru",
                    albumImageBig = "big.ru"
                ),
                Track(
                    id = 123,
                    name = "TestTrack2",
                    artistName = "TestArtist2",
                    albumName = "TestCollection2",
                    link = "test.ru",
                    duration = "4:33",
                    audioPreview = "test3.ru",
                    image = "test4.ru",
                    albumImageSmall = "small.ru",
                    albumImageMedium = "medium.ru",
                    albumImageBig = "big.ru"
                ),
                Track(
                    id = 123,
                    name = "TestTrack3",
                    artistName = "TestArtist3",
                    albumName = "TestCollection3",
                    link = "test.ru",
                    duration = "4:33",
                    audioPreview = "test3.ru",
                    image = "test4.ru",
                    albumImageSmall = "small.ru",
                    albumImageMedium = "medium.ru",
                    albumImageBig = "big.ru"
                ),
            )
        )
    }
}