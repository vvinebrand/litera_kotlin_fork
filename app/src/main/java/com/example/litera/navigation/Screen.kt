package com.example.litera.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val label: String = "",
    val icon: ImageVector? = null
) {
    object First     : Screen("first")
    object Home      : Screen("home",    "Главная",    Icons.Default.Home)
    object Library   : Screen("library", "Библиотека", Icons.Default.Create)
    object Search    : Screen("search",  "Поиск",      Icons.Default.Search)
    object Profile   : Screen("profile", "Профиль",    Icons.Default.Person)

    object Collections : Screen("collections")
    object Auth        : Screen("auth")       // ←  есть!
    object Register    : Screen("register")   // ←  есть!
}


val bottomScreens = listOf(Screen.Home, Screen.Library, Screen.Search, Screen.Profile)
