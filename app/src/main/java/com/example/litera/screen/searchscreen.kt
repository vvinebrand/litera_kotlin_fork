package com.example.litera.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
// Убедитесь, что это правильный путь к вашему BottomNavItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen() {
    var selectedItemIndex by remember { mutableStateOf(2) } // Индекс 2 соответствует "Поиск"
    var searchText by remember { mutableStateOf("") } // Состояние для текста в поле поиска

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
                actions = {
                    // Иконка поиска в TopAppBar может быть не нужна, если есть поле ввода.
                    // Оставлю как пример, но вы можете ее убрать.
                    IconButton(onClick = { /* TODO: Действие для поиска */ }) {
                        Icon(
                            Icons.Filled.Search,
                            contentDescription = "Поиск",
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
                .padding(innerPadding) // Важно для отступов от TopAppBar/BottomBar
                .padding(horizontal = 16.dp) // Горизонтальные отступы для контента
        ) {
            // Поле поиска располагается сразу под TopAppBar
            Spacer(modifier = Modifier.height(16.dp)) // Отступ от TopAppBar
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it }, // Обновляем состояние текста
                label = { Text("Искать книги, авторов...") }, // Подсказка
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Поиск") }, // Иконка в начале поля
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp)) // Отступ после поля поиска

            // Здесь будут результаты поиска или другие элементы контента
            Text(
                text = "Введите запрос для поиска",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            // Здесь можно добавить LazyColumn/LazyVerticalGrid для отображения результатов
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SearchScreenPreview() {
    SearchScreen()
}