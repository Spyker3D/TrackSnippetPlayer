package com.spyker3d.tracksnippetplayer.audioplayer.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spyker3d.tracksnippetplayer.ui.theme.TrackSnippetPlayerTheme

@Composable
fun AudioPlayerScreen(
    modifier: Modifier = Modifier,
    trackId: Int,
    onBackPressed: () -> Unit
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
    LargeTopAppBar(
        modifier =
        modifier
            .padding(top = 0.dp)
            .wrapContentHeight(),
        title = {
            Text(
                modifier = Modifier.padding(horizontal = 0.dp),
                text = "Аудиоплеер",
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
        AudioPlayerScreen(
            trackId = 123,
            onBackPressed = {})
    }
}