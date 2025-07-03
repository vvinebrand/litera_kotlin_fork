// MainActivity.kt
package com.example.litera

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.example.litera.navigation.bottomScreens
import com.example.litera.components.LiteraBottomBar
import com.example.litera.screen.*
import com.example.litera.navigation.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val backEntry     by navController.currentBackStackEntryAsState()
            val currentRoute  = backEntry?.destination?.route

            val showBottomBar = currentRoute in bottomScreens.map { it.route }

            Scaffold(
                bottomBar = {
                    if (showBottomBar) {
                        LiteraBottomBar(navController)
                    }
                }
            ) { innerPadding ->
                NavHost(
                    navController,
                    startDestination = Screen.First.route,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable(Screen.First.route)      { FirstScreen(navController) }
                    composable(Screen.Home.route)       { HomeScreen() }
                    composable(Screen.Library.route)    { LibraryScreen(navController) }
                    composable(Screen.Search.route)     { SearchScreen() }
                    composable(Screen.Profile.route)    { AccountScreen() }
                    composable(Screen.Collections.route){ CollectionsScreen(navController) }
                }
            }

        }
    }
}
