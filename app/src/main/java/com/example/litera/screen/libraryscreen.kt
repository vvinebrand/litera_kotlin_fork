package com.example.litera.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Menu

// Импорты для BottomNavigationBar из вашего примера
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Create // Иконка для "Библиотека" из вашего примера
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Person

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// import com.example.litera.ui.theme.BackgroundColor // Закомментировано, так как вы используете LightGray
// import com.example.litera.ui.theme.PrimaryRed     // Закомментировано, так как вы используете Color.Red или явные HEX

// Data class для представления книги в сетке (реальные данные будут тут)
data class Book(
    val title: String,
    val progress: String, // Например, "67%"
    val coverColor: Color = Color.LightGray // Заглушка для обложки
)

@OptIn(ExperimentalMaterial3Api::class) // Нужен для Scaffold
@Composable
fun LibraryScreen() {
    // Состояние для отслеживания выбранного пункта навигации
    // Здесь мы устанавливаем "Библиотека" (индекс 1) как выбранный по умолчанию для LibraryScreen
    var selectedItemIndex by remember { mutableStateOf(1) } // Индекс 1 соответствует "Библиотека" в вашем BottomNavigationBar

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Библиотека",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C3E50) // Цвет текста из скриншота
                    )
                },
                actions = {
                    IconButton(onClick = { /* TODO: Действие поделиться */ }) {
                        Icon(
                            Icons.Filled.Share, // Иконка "поделиться"
                            contentDescription = "Поделиться",
                            tint = Color(0xFF2C3E50) // Цвет иконки
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White // Фон верхней панели (из примера BottomNavigationBar)
                )
            )
        },
        bottomBar = {
            // Использование вашей реализации BottomNavigationBar
            // Здесь мы передаем текущий выбранный индекс и лямбду для обновления
            BottomNavigationBar(selectedItemIndex = selectedItemIndex) { index ->
                selectedItemIndex = index
                // TODO: Здесь будет логика навигации к соответствующему экрану
                // например: navController.navigate(bottomNavItems[index].route)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FDF5)) // Светлый фон, как в вашем примере HomeScreen
                .padding(innerPadding) // Важно для отступов от TopAppBar/BottomBar
        ) {
            // Раздел "Коллекции"
            CollectionsSection()

            // Отступ между разделом коллекций и сеткой книг
            Spacer(modifier = Modifier.height(16.dp))

            // Сетка книг (заглушка)
            BooksGrid()
        }
    }
}

// CollectionsSection и BooksGrid остаются без изменений
@Composable
fun CollectionsSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(Color(0xFFF8FDF5)) // Фон как у всего экрана
    ) {
        Divider(color = Color.LightGray, thickness = 1.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clickable { /* TODO: Перейти к коллекциям */ }
                .padding(horizontal = 0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Filled.Menu,
                contentDescription = "Коллекции",
                tint = Color(0xFF2C3E50),
                modifier = Modifier.padding(start = 0.dp, end = 8.dp)
            )
            Text(
                text = "Коллекции",
                fontSize = 16.sp,
                color = Color(0xFF2C3E50),
                modifier = Modifier.weight(1f)
            )
            Icon(
                Icons.Filled.KeyboardArrowRight,
                contentDescription = "Перейти к коллекциям",
                tint = Color(0xFF2C3E50)
            )
        }
        Divider(color = Color.LightGray, thickness = 1.dp)
    }
}

@Composable
fun BooksGrid() {
    // Заглушка для списка книг
    val books = List(10) {
        Book(
            title = "Название ${it + 1}",
            progress = "${(it + 1) * 10}%",
            coverColor = Color.LightGray.copy(alpha = 0.7f)
        )
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(books) { book ->
            BookCard(book = book)
        }
    }
}

@Composable
fun BookCard(book: Book) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.7f)
                .background(book.coverColor, shape = RoundedCornerShape(8.dp))
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = book.title,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = Color(0xFF2C3E50)
        )
        Text(
            text = book.progress,
            fontSize = 12.sp,
            color = Color.Gray
        )
        // Иконка "..."
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.CenterEnd)
        ) {
            IconButton(onClick = { /* TODO: Показать меню для книги */ }) {
                Text(text = "...", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
            }
        }
    }
}

// Ваша реализация BottomNavigationBar из примера HomeScreen, но с параметрами
@Composable
fun BottomNavigationBar(selectedItemIndex: Int, onItemSelected: (Int) -> Unit) {
    NavigationBar(containerColor = Color.White) {
        val items = listOf(
            Pair("Главная", Icons.Default.Home),
            Pair("Библиотека", Icons.Default.Create),
            Pair("Поиск", Icons.Default.Search),
            Pair("Профиль", Icons.Default.Person)
        )

        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(item.second, contentDescription = item.first) },
                selected = selectedItemIndex == index,
                onClick = { onItemSelected(index) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Red,    // Цвет выбранной иконки
                    selectedTextColor = Color.Red,    // Цвет выбранного текста
                    unselectedIconColor = Color.Gray, // Цвет невыбранной иконки
                    unselectedTextColor = Color.Gray, // Цвет невыбранного текста
                    indicatorColor = Color.Transparent // Цвет индикатора
                )
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, device = "id:pixel_6")
@Composable
fun LibraryScreenPreview() {
    LibraryScreen()
}