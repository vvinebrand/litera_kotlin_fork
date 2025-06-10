package com.example.litera.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack // Импорт для иконки "назад"
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionsScreen() {
    var selectedItemIndex by remember { mutableIntStateOf(1) } // Индекс 1 соответствует "Библиотеке"

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Библиотека", // Текст заголовка, как на image_ab8ffe.png
                        fontSize = 28.sp, // Или 20.sp, чтобы было похоже на скриншот
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C3E50)
                    )
                },
                navigationIcon = { // Добавляем кнопку "назад"
                    IconButton(onClick = { /* TODO: Логика возврата назад (например, navController.popBackStack()) */ }) {
                        Icon(
                            Icons.Filled.ArrowBack, // Иконка стрелки назад
                            contentDescription = "Назад к Библиотеке",
                            tint = Color(0xFF2C3E50)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(selectedItemIndex = selectedItemIndex) { index ->
                selectedItemIndex = index
                // TODO: Здесь будет логика навигации к соответствующему экрану
            }
        }
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
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Например, список ваших книжных коллекций",
                fontSize = 16.sp,
                color = Color.Gray
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CollectionsScreenPreview() {
    CollectionsScreen()
}