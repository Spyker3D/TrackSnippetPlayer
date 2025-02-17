package com.spyker3d.tracksnippetplayer.audioplayer.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.spyker3d.tracksnippetplayer.R
import com.spyker3d.tracksnippetplayer.common.domain.model.Track
import com.spyker3d.tracksnippetplayer.search.presentation.DOWNLOADS_SCREEN
import com.spyker3d.tracksnippetplayer.search.presentation.LoadingScreen
import com.spyker3d.tracksnippetplayer.search.presentation.SEARCH_SCREEN
import com.spyker3d.tracksnippetplayer.ui.theme.TrackSnippetPlayerTheme
import com.spyker3d.tracksnippetplayer.utils.makeToast
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun AudioPlayerScreen(
    modifier: Modifier = Modifier,
    trackId: Long,
    trackPreviewUrl: String,
    onBackPressed: () -> Unit,
    playbackState: PlaybackState,
    onSeekTo: (Long) -> Unit,
    onRewind: () -> Unit,
    onPlayPause: () -> Unit,
    onFastForward: () -> Unit,
    isDownloadsScreen: Boolean,
    prepareTrack: (String, String, String, Long, Boolean) -> Unit,
    trackState: TrackState,
    showToast: SharedFlow<Int>,
    onDeleteTrack: (Track) -> Unit,
    onDownloadTrack: (Track) -> Unit,
    onNextTrack: () -> Unit,
    onPreviousTrack: () -> Unit
) {
    val navScreenInfo = if (isDownloadsScreen) DOWNLOADS_SCREEN else SEARCH_SCREEN

    BackHandler {
        onBackPressed.invoke()
    }

    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.surface,
            topBar = {
                TopBar(onBackPressed = onBackPressed)
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
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        when (trackState) {
                            is TrackState.Success -> {
                                val track = trackState.data

                                if (!isDownloadsScreen) {
                                    LaunchedEffect(trackPreviewUrl) {
                                        prepareTrack(
                                            trackPreviewUrl,
                                            track.name,
                                            track.artistName,
                                            track.id,
                                            isDownloadsScreen
                                        )
                                    }
                                } else {
                                    LaunchedEffect(track.uriDownload) {
                                        prepareTrack(
                                            track.uriDownload,
                                            track.name,
                                            track.artistName,
                                            track.id,
                                            isDownloadsScreen
                                        )
                                    }
                                }

                                showTrackInfo(
                                    onDeleteListener = onDeleteTrack,
                                    onDownloadListener = onDownloadTrack,
                                    onPlayPauseListener = onPlayPause,
                                    onBackPressedListener = onBackPressed,
                                    playbackState = playbackState,
                                    isDownloadsScreen = isDownloadsScreen
                                )
                            }

                            TrackState.Loading -> LoadingScreen(modifier = modifier)
                            TrackState.Error -> Unit
                            TrackState.Idle -> Unit
                        }
                        Slider(
                            value = if (playbackState.duration > 0)
                                playbackState.currentPosition.toFloat() / playbackState.duration.toFloat()
                            else 0f,
                            onValueChange = { fraction ->
                                val newPosition = (fraction * playbackState.duration).toLong()
                                onSeekTo(newPosition)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = formatTime(playbackState.currentPosition),
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = formatTime(playbackState.duration),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Spacer(modifier = Modifier.height(32.dp))
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .clickable { onPreviousTrack.invoke() },
                                painter = painterResource(id = R.drawable.png_previous_100),
                                contentDescription = "Previous track",
                            )
                            Image(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .clickable { onRewind.invoke() },
                                painter = painterResource(id = R.drawable.png_rewind_100),
                                contentDescription = "Rewind",
                            )
                            Image(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .clickable {
                                        onPlayPause.invoke()
                                    },
                                painter = if (playbackState.isPlaying) {
                                    painterResource(id = R.drawable.png_pause_100)
                                } else {
                                    painterResource(id = R.drawable.png_play_100)
                                },
                                contentDescription = "Play/Pause",
                            )
                            Image(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .clickable { onFastForward.invoke() },
                                painter = painterResource(id = R.drawable.png_fast_forward_100),
                                contentDescription = "FastForward",
                            )
                            Image(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .clickable { onNextTrack.invoke() },
                                painter = painterResource(id = R.drawable.png_next_100),
                                contentDescription = "Next track",
                            )
                        }
                    }
                }
                val context = LocalContext.current
                LaunchedEffect(Unit) {
                    showToast.collect { messageId ->
                        makeToast(
                            context = context,
                            errorId = messageId
                        )
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun showTrackInfo(
    onDeleteListener: (Track) -> Unit,
    onDownloadListener: (Track) -> Unit,
    onPlayPauseListener: () -> Unit,
    onBackPressedListener: () -> Unit,
    playbackState: PlaybackState,
    isDownloadsScreen: Boolean,
) {
    GlideImage(
        modifier = Modifier
            .clip(RoundedCornerShape(2.dp))
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(16.dp),
        model = playbackState.albumImage,
        contentScale = ContentScale.Fit,
        loading = placeholder(R.drawable.ic_placeholder_audioplayer),
        failure = placeholder(R.drawable.ic_placeholder_audioplayer),
        contentDescription = playbackState.albumName
    )
    Spacer(modifier = Modifier.padding(horizontal = 16.dp))
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = playbackState.trackName,
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontSize = 36.sp,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Row {
                if (!isDownloadsScreen) {
                    IconButton(onClick = { onDownloadListener(playbackState.currentTrack!!) }) {
                        Icon(
                            imageVector = Icons.Filled.Download,
                            contentDescription = "Загрузить трек"
                        )
                    }
                } else {
                    IconButton(onClick = {
                        if (playbackState.isPlaying) {
                            onPlayPauseListener.invoke()
                        }
                        onDeleteListener(playbackState.currentTrack!!)
                        onBackPressedListener.invoke()

                    }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Удалить трек из загрузок"
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                text = playbackState.artistName,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                painter = painterResource(id = R.drawable.ic_divider),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .size(8.dp)
                    .align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                text = playbackState.trackTime
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    modifier: Modifier = Modifier, onBackPressed: () -> Unit
) {
    TopAppBar(
        modifier =
        modifier
            .padding(top = 20.dp, bottom = 20.dp, start = 10.dp)
            .wrapContentHeight(),
        title = {
            Text(
                modifier = Modifier.padding(horizontal = 0.dp),
                text = "Аудиоплеер",
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium,
                lineHeight = 44.sp,
            )
        },
        colors =
        TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Назад"
                )
            }
        }
    )
}

private fun formatTime(timeMs: Long): String {
    val totalSeconds = timeMs / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}

@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
@Composable
fun CurrentPurchasesListScreenScaffoldPreview() {
    TrackSnippetPlayerTheme {
        AudioPlayerScreen(
            trackId = 123,
            trackPreviewUrl = "test.ru",
            onBackPressed = {},
            playbackState = PlaybackState(
                currentTrack = Track(
                    id = 123,
                    name = "test track",
                    artistName = "test artist",
                    albumName = "test album",
                    albumImageSmall = "test image small",
                    albumImageMedium = "test image medium",
                    albumImageBig = "test image big",
                    link = "test lint.ru",
                    duration = "60",
                    audioPreview = "audio preview url",
                    image = "image test"
                )
            ),
            onRewind = {},
            onPlayPause = {},
            onFastForward = {},
            onSeekTo = {},
            isDownloadsScreen = true,
            prepareTrack = { string1, string2, string3, long, boolean -> Unit },
            trackState = TrackState.Success(
                Track(
                    id = 123,
                    name = "test track",
                    artistName = "test artist",
                    albumName = "test album",
                    albumImageSmall = "test image small",
                    albumImageMedium = "test image medium",
                    albumImageBig = "test image big",
                    link = "test lint.ru",
                    duration = "60",
                    audioPreview = "audio preview url",
                    image = "image test"
                )
            ),
            showToast = MutableSharedFlow<Int>(),
            onDeleteTrack = { },
            onDownloadTrack = { },
            onPreviousTrack = { },
            onNextTrack = { }
        )
    }
}