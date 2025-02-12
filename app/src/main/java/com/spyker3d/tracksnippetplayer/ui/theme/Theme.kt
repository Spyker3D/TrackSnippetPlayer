package com.spyker3d.tracksnippetplayer.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = white,
    onPrimary = cyan,
    surface = blackDarkTheme,
    onSurface = white,
    onSurfaceVariant = darkWhite,
    onTertiary = white,
    outlineVariant = grey,
    secondary = lightGrey,
    onSecondary = greyDark,
    secondaryContainer = lightBlue,
    onSecondaryContainer = white,
    tertiaryContainer = white, // контейнер выбранной единицы измерения в счетчике
    onTertiaryContainer = black,
    surfaceVariant = blueDark,
    onBackground = blue, // для стрелки вниз "создать первый список" при отсутствии списков
    onError = red,
    errorContainer = red,
    outline = greyStrokeDark, // граница для счетчика количества
    inversePrimary = blue, // для кнопки "+ Добавить продукт", кнопок + и < > на главном экране со списками
    surfaceContainer = cyan, // для иконки удаления при свайпе
)

private val LightColorScheme = lightColorScheme(
    primary = cyan,
    onPrimary = white,
    surface = white,
    onSurface = onSurface,
    onSurfaceVariant = grey,
    onTertiary = black,
    outlineVariant = outlineVariant,
    secondary = lightGrey,
    onSecondary = white,
    secondaryContainer = lightBlue,
    onSecondaryContainer = white,
    tertiaryContainer = white, // контейнер выбранной единицы измерения в счетчике
    onTertiaryContainer = blackLightForCounter, // для выбранной единицы измерения в счетчике
    surfaceVariant = lightBlue,
    onBackground = blue, // для стрелки вниз "создать первый список" при отсутствии списков
    onError = red,
    errorContainer = red,
    outline = lightOutline, // граница для счетчика количества
    inversePrimary = cyan, // для кнопки "+ Добавить продукт", кнопок + и < > на главном экране со списками
    surfaceContainer = cyan, // для иконки удаления при свайпе
)

@Composable
fun TrackSnippetPlayerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

    MaterialTheme(
      colorScheme = colorScheme,
      typography = Typography,
      content = content
    )
}