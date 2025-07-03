// ui/components/LiteraBottomBar.kt
package com.example.litera.components

import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.litera.navigation.bottomScreens
import com.example.litera.navigation.Screen

@Composable
fun LiteraBottomBar(navController: NavController) {
    val backEntry by navController.currentBackStackEntryAsState()
    val current = backEntry?.destination?.route

    NavigationBar(containerColor = Color.White) {
        bottomScreens.forEach { screen ->
            val selected = screen.route == current
            NavigationBarItem(
                icon     = { Icon(screen.icon!!, contentDescription = screen.label) },
                label    = { Text(screen.label) },
                selected = selected,
                onClick  = { /* навигация как раньше */ },
                colors   = NavigationBarItemDefaults.colors(
                    selectedIconColor   = Color.Red,
                    selectedTextColor   = Color.Red,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    indicatorColor      = Color.Transparent
                )
            )
        }
    }
}
