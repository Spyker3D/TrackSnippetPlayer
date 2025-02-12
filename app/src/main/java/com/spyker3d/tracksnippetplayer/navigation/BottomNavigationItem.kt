package com.spyker3d.tracksnippetplayer.navigation

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavigationItem<T : Any>(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: T
)