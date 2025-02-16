package com.spyker3d.tracksnippetplayer.root

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.spyker3d.tracksnippetplayer.navigation.AppNavHost
import com.spyker3d.tracksnippetplayer.ui.theme.TrackSnippetPlayerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Для Android 13+ (API 33 и выше) требуется запрос разрешения POST_NOTIFICATIONS
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    REQUEST_CODE_POST_NOTIFICATIONS
                )
            }

            setContent {
                TrackSnippetPlayerTheme {
                    val navController = rememberNavController()
                    // Если extra "openAudioPlayer" == true, открываем экран аудиоплеера
                    LaunchedEffect(Unit) {
                        intent.extras?.takeIf { it.getBoolean("openAudioPlayer", false) }?.let {
                            val trackId = it.getString("trackName", "")
                            val trackPreviewUrl = it.getString("trackPreviewUrl") ?: ""
                            val isDownloadedScreen = it.getBoolean("isDownloadedScreen")
                            navController.navigate("audioPlayer/$trackId/$trackPreviewUrl/$isDownloadedScreen")
                        }
                    }
                    AppNavHost()
                }
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_POST_NOTIFICATIONS = 1001
    }
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TrackSnippetPlayerTheme {
        AppNavHost()
    }
}