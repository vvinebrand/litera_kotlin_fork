package com.example.litera.components

import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.litera.navigation.bottomScreens

@Composable
fun LiteraBottomBar(navController: NavController) {
    val backEntry   by navController.currentBackStackEntryAsState()
    val currentRoute = backEntry?.destination?.route

    NavigationBar(containerColor = Color.White) {
        bottomScreens.forEach { screen ->
            val selected = screen.route == currentRoute

            NavigationBarItem(
                icon  = { Icon(screen.icon!!, null) },
                label = { screen.label?.let { Text(it) } },     // ← label уже non-null
                selected = selected,
                onClick = {
                    if (!selected) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
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
