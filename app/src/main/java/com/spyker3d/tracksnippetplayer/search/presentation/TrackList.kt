package com.spyker3d.tracksnippetplayer.search.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
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
import com.spyker3d.tracksnippetplayer.ui.theme.TrackSnippetPlayerTheme

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun TrackList(
    listOfTracks: List<Track>,
    isDeleteIconVisible: Boolean,
    onDeleteItemListener: (Long) -> Unit,
    onClickListener: (Long, String) -> Unit,
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
                        .height(64.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clickable { onClickListener(track.id, track.audioPreview) },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        GlideImage(
                            modifier = Modifier
                                .clip(RoundedCornerShape(2.dp))
                                .size(45.dp),
                            model = track.albumImageMedium,
                            contentScale = ContentScale.Fit,
                            loading = placeholder(R.drawable.ic_placeholder_track_list),
                            failure = placeholder(R.drawable.ic_placeholder_track_list),
                            contentDescription = track.albumName
                        )
                        Spacer(modifier = Modifier.padding(horizontal = 16.dp))
                        Column{
                            Text(
                                text = track.name,
                                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Text(
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    text = track.artistName,
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
                                    text = track.duration
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }

                    if (isDeleteIconVisible) {
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
                )
            ),
            isDeleteIconVisible = true,
            onDeleteItemListener = { },
            onClickListener = { int, string -> Unit }
        )
    }
}