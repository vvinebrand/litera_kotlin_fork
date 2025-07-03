/**
 * CollectionsScreen.kt
 *
 * Переход назад к LibraryScreen теперь возможен
 *  • по стрелке ←,
 *  • по самому заголовку «Библиотека»,
 *  • свайпом вправо от любой точки экрана.
 *
 *  BottomBar здесь отсутствует (он общий в MainActivity).
 *
 *  В NavHost регистрируется так:
 *      composable(Screen.Collections.route) { CollectionsScreen(navController) }
 */

package com.example.litera.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.clickable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.litera.navigation.Screen   // ваш синглтон с route-ами

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionsScreen(navController: NavController) {

    // ─── обработка свайпа вправо ─────────────────────────────
    val swipeToBackModifier = Modifier.pointerInput(Unit) {
        detectHorizontalDragGestures { _, dragAmount ->
            if (dragAmount > 100) {            // порог ~100px
                navController.popBackStack()
            }
        }
    }

    Scaffold(
        modifier = swipeToBackModifier,         // жест «свайп вправо»
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Библиотека",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C3E50),
                        modifier = Modifier.clickable {   // тап по заголовку
                            navController.popBackStack()
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {               // кнопка ←
                        navController.popBackStack()
                    }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Назад",
                            tint = Color(0xFF2C3E50)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
        // bottomBar нет: он общий для всего приложения
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FDF5))
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Содержимое коллекций будет здесь!",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Например, список ваших книжных коллекций",
                fontSize = 16.sp,
                color = Color.Gray
            )
        }
    }
}

/* ─── Preview ─────────────────────────────────────────────── */

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CollectionsScreenPreview() {
    val navController = rememberNavController()
    CollectionsScreen(navController)
}
