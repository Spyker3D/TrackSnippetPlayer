package com.spyker3d.tracksnippetplayer.apitracks.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spyker3d.tracksnippetplayer.R
import com.spyker3d.tracksnippetplayer.ui.theme.TrackSnippetPlayerTheme

@Composable
fun SearchTextField(
    text: String,
    hint: String,
    onTextChanged: (String) -> Unit,
) {
    BasicTextField(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(28.dp))
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondary),
        value = text,
        onValueChange = onTextChanged,
        singleLine = true,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.onTertiaryContainer),
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onTertiaryContainer
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .height(56.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.padding(
                        start = 16.dp,
                        top = 16.dp,
                        bottom = 16.dp,
                        end = 12.dp
                    ),
                    painter = painterResource(id = R.drawable.ic_search_tab_unselected),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Box(
                    modifier = Modifier
                        .weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (text.isEmpty()) {
                        Text(
                            text = hint,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                    innerTextField()
                }
                if (text.isNotEmpty()) {
                    Icon(
                        modifier = Modifier
                            .padding(
                                start = 16.dp,
                                top = 16.dp,
                                bottom = 16.dp,
                                end = 12.dp
                            )
                            .clickable { onTextChanged("") },
                        imageVector = Icons.Filled.Close,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    )

}

@Preview(showBackground = true)
@Composable
private fun PreviewSearchTextField() {
    TrackSnippetPlayerTheme {
        SearchTextField(text = "sdaf", hint = "", onTextChanged = {})
    }
}