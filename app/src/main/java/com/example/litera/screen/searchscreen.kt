/**
 * SearchScreen.kt
 * Экран поиска: поле ввода + заглушка «Введите запрос…».
 *  • Собственного BottomBar-а больше нет (общий в MainActivity).
 *  • Навигация на другие экраны через нижнюю панель, поэтому NavController
 *    здесь пока не нужен. Если позже будете открывать «BookDetails», просто
 *    добавьте параметр navController.
 */

package com.example.litera.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen() {

    var searchText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Поиск",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C3E50)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
        // bottomBar отсутствует – общий бар в MainActivity
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FDF5))
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Искать книги, авторов...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Поиск")
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            if (searchText.isBlank()) {
                Text(
                    text = "Введите запрос для поиска",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                // TODO: вывод результатов поиска
            }
        }
    }
}

/* ─── Preview ─────────────────────────────────────────────── */

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SearchScreenPreview() {
    SearchScreen()
}
