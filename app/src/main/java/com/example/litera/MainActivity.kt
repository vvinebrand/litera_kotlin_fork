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
import com.example.litera.components.LiteraBottomBar
import com.example.litera.navigation.Screen
import com.example.litera.navigation.bottomScreens
import com.example.litera.screen.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            /* ───── 1. Один-единственный NavController ───── */
            val navController = rememberNavController()

            /* ───── 2. Следим за текущим маршрутом ───── */
            val backEntry  by navController.currentBackStackEntryAsState()
            val current    = backEntry?.destination?.route
            val showBar    = current in bottomScreens.map { it.route }

            Scaffold(
                bottomBar = { if (showBar) LiteraBottomBar(navController) }
            ) { inner ->
                /* ───── 3. NavHost со ВСЕМИ экранами ───── */
                NavHost(
                    navController,
                    startDestination = Screen.First.route,
                    modifier = Modifier.padding(inner)
                ) {
                    composable(Screen.First.route)       { FirstScreen(navController) }

                    composable(Screen.Auth.route)        { AuthScreen(navController) }
                    composable(Screen.Register.route)    { RegistrationScreen(navController) }

                    composable(Screen.Home.route)        { HomeScreen() }
                    composable(Screen.Library.route)     { LibraryScreen(navController) }
                    composable(Screen.Search.route)      { SearchScreen() }
                    composable(Screen.Profile.route)     { AccountScreen(navController) }
                    composable(Screen.Collections.route) { CollectionsScreen(navController) }
                    composable("reader/{path}") {
                        val path = it.arguments?.getString("path")!!
                        BookReaderScreen(path)
                    }
                }


            }
        }
    }
}
