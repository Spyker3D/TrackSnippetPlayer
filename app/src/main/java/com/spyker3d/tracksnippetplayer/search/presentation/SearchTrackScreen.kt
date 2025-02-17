package com.spyker3d.tracksnippetplayer.search.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.spyker3d.tracksnippetplayer.R
import com.spyker3d.tracksnippetplayer.common.domain.model.Track
import com.spyker3d.tracksnippetplayer.ui.theme.TrackSnippetPlayerTheme
import com.spyker3d.tracksnippetplayer.utils.makeToast
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

const val SEARCH_SCREEN = "SearchScreen"
const val DOWNLOADS_SCREEN = "DownloadsScreen"

@Composable
fun SearchTrackScreen(
    modifier: Modifier = Modifier,
    onNavigateToAudioPlayer: (trackId: Long, trackPreviewUrl: String, trackListIds: List<Long>, trackListsPreview: List<String>) -> Unit,
    searchState: SearchState,
    onSearchTrack: (trackName: String) -> Unit,
    showToast: SharedFlow<Int>,
    onUpdateTrackList: (List<Track>) -> Unit,
    navScreenInfo: String,
    navController: NavHostController,
    lastSearchedText: String
) {
    Box(
        modifier = modifier,
    ) {
        // список треков для AudioPlayerService обновляется только если переходим с экрана загрузок,
        // если переходим с экрана плеера, на который попали с экрана поиска, обновления не происходит,
        // список сохраняется старый
        LaunchedEffect(Unit) {
            if (searchState is SearchState.Content && navScreenInfo != SEARCH_SCREEN) {
                val trackList = (searchState as SearchState.Content).trackList
                onUpdateTrackList(trackList)
            } else {
                navController.currentBackStackEntry?.savedStateHandle?.remove<String>("fromAudioPlayer")
            }
        }
        Scaffold(
            containerColor = MaterialTheme.colorScheme.surface,
            topBar = { TopBar() },
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
                    var inputText by remember { mutableStateOf(lastSearchedText) }
                    Column {
                        SearchTextField(
                            text = inputText,
                            onTextChanged = { text ->
                                inputText = text
                                onSearchTrack(inputText)
                            },
                            hint = stringResource(id = R.string.search_hint)
                        )
                        Spacer(modifier = Modifier.padding(8.dp))

                        when (searchState) {
                            is SearchState.Content -> {
                                val trackListIds = searchState.trackList.map { it.id }
                                val trackListUrls = searchState.trackList.map { it.audioPreview }
                                TrackList(
                                    listOfTracks = searchState.trackList,
                                    isDeleteIconVisible = false,
                                    onDeleteItemListener = {},
                                    onClickListener = { trackId, trackAudioPreview ->
                                        onNavigateToAudioPlayer(
                                            trackId,
                                            trackAudioPreview,
                                            trackListIds,
                                            trackListUrls
                                        )
                                    }
                                )
                            }

                            SearchState.Empty -> Unit
                            SearchState.Loading -> LoadingScreen(modifier = modifier)
                            SearchState.Error -> Unit
                        }

                        val context = LocalContext.current
                        LaunchedEffect(Unit) {
                            showToast.collect { messageId ->
                                makeToast(context = context, errorId = messageId)
                            }
                        }
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
                text = "Поиск треков",
                fontSize = 44.sp,
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
        SearchTrackScreen(
            onNavigateToAudioPlayer = { int, string, trackListIds, trackListUrls -> Unit },
            onSearchTrack = {},
            searchState = SearchState.Content(
                listOf(
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
            ),
            showToast = MutableSharedFlow<Int>(),
            onUpdateTrackList = { },
            navScreenInfo = "",
            navController = rememberNavController(),
            lastSearchedText = ""
        )
    }
}