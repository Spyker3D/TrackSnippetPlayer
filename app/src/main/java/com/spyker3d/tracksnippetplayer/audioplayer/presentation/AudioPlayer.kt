package com.spyker3d.tracksnippetplayer.audioplayer.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.spyker3d.tracksnippetplayer.R
import com.spyker3d.tracksnippetplayer.ui.theme.TrackSnippetPlayerTheme

@Composable
fun AudioPlayerScreen(
    modifier: Modifier = Modifier,
    trackId: Int,
    trackPreviewUrl: String,
    onBackPressed: () -> Unit,
    playbackState: PlaybackState,
    onSeekTo: (Long) -> Unit,
    onRewind: () -> Unit,
    onPlayPause: () -> Unit,
    onFastForward: () -> Unit,
    isDownloadsScreen: Boolean,
    exoPlayer: ExoPlayer
) {
    Box(
        modifier = modifier,
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
                        // Слайдер для отображения прогресса воспроизведения
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
                        // Отображение текущего времени и общей длительности
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
                        // Кнопки управления плеером
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (isDownloadsScreen) {
                                Image(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .clickable {  }, // дописать логику переключения на другой трек
                                    painter = painterResource(id = R.drawable.png_previous_100),
                                    contentDescription = "Rewind",
                                )
                            }
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
                                    .clickable { onPlayPause.invoke() },
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
                            if (isDownloadsScreen) {
                                Image(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .clickable {  }, // дописать логику переключения на другой трек
                                    painter = painterResource(id = R.drawable.png_next_100),
                                    contentDescription = "Rewind",
                                )
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
            playbackState = PlaybackState(),
            onRewind = {},
            onPlayPause = {},
            onFastForward = {},
            onSeekTo = {},
            isDownloadsScreen = true,
            exoPlayer = ExoPlayer.Builder(LocalContext.current).build()
        )
    }
}