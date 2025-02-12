package com.spyker3d.tracksnippetplayer.apitracks.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spyker3d.tracksnippetplayer.R
import com.spyker3d.tracksnippetplayer.apitracks.domain.model.Track
import com.spyker3d.tracksnippetplayer.ui.theme.TrackSnippetPlayerTheme

@Composable
fun TrackList(
    listOfTracks: List<Track>,
    isDeleteIconVisible: Boolean,
    onDeleteItemListener: (Int) -> Unit,
    onClickListener: (Int) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            horizontal = 16.dp,
            vertical = 0.dp
        ),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
       items(listOfTracks) { track ->
           Column(
               modifier = Modifier
                   .background(MaterialTheme.colorScheme.surface)
           ) {
               Row(
                   modifier = Modifier
                       .height(56.dp)
                       .fillMaxWidth()
                       .padding(horizontal = 16.dp)
                       .clickable { onClickListener(track.id) },
                   verticalAlignment = Alignment.CenterVertically,
                   horizontalArrangement = Arrangement.SpaceBetween
               ) {
                   Text(
                       modifier = Modifier
                           .padding(vertical = 8.dp)
                           .weight(1f),
                       text = track.name,
                       fontFamily = FontFamily(Font(R.font.roboto_regular)),
                       fontSize = 16.sp,
                       color = MaterialTheme.colorScheme.onSurface,
                       maxLines = 1,
                       overflow = TextOverflow.Ellipsis
                   )
                   if(isDeleteIconVisible) {
                       Icon(
                           modifier = Modifier
                               .padding(
                                   start = 16.dp,
                                   top = 16.dp,
                                   bottom = 16.dp,
                                   end = 12.dp
                               )
                               .clickable { onDeleteItemListener.invoke(track.id) },
                           imageVector = Icons.Filled.Delete,
                           contentDescription = null,
                           tint = MaterialTheme.colorScheme.onSurfaceVariant,
                       )
                   }
               }
               HorizontalDivider(
                   modifier = Modifier
                       .fillMaxWidth(),
                   color = MaterialTheme.colorScheme.outlineVariant,
                   thickness = 0.5.dp
               )
           }
        }
    }
}

@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
@Composable
fun TrackListPreview() {
    TrackSnippetPlayerTheme {
        TrackList(
            listOfTracks = listOf(
                Track(
                    id = 123,
                    name = "TestTrack",
                    artistName = "TestArtist",
                    albumName = "TestCollection",
                    link = "test.ru",
                    duration = "4:33",
                    audioPreview = "test3.ru",
                    image = "test4.ru"
                ),
                Track(
                    id = 123,
                    name = "TestTrack2",
                    artistName = "TestArtist2",
                    albumName = "TestCollection2",
                    link = "test.ru",
                    duration = "4:33",
                    audioPreview = "test3.ru",
                    image = "test4.ru"
                ),
                Track(
                    id = 123,
                    name = "TestTrack3",
                    artistName = "TestArtist3",
                    albumName = "TestCollection3",
                    link = "test.ru",
                    duration = "4:33",
                    audioPreview = "test3.ru",
                    image = "test4.ru"
                )
            ),
            isDeleteIconVisible = true,
            onDeleteItemListener = { },
            onClickListener = { }
        )
    }
}