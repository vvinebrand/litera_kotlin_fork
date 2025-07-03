/**
 * LibraryScreen.kt
 * Показывает библиотеку книг и умеет:
 *  • открывать экран «Коллекции» при клике по соответствующей строке;
 *  • подсвечивать иконку «Библиотека» в нижней навигации (делается централизованно);
 *  • НЕ содержит собственный BottomBar – он вынесен в MainActivity.
 *
 *  Обязательное условие: этот Composable вызывается из NavHost так:
 *      composable(Screen.Library.route) { LibraryScreen(navController) }
 */

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
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.litera.navigation.Screen // ← пакет с вашими маршрутами

// ─────────────────────────────────────────────────────────────
// Модель книги (заглушка)
data class Book(
    val title: String,
    val progress: String,
    val coverColor: Color = Color.LightGray
)
// ─────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(navController: NavController) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Библиотека",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C3E50)
                    )
                },
                actions = {
                    IconButton(onClick = { /* TODO: Поделиться */ }) {
                        Icon(
                            Icons.Filled.Share,
                            contentDescription = "Поделиться",
                            tint = Color(0xFF2C3E50)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
        // bottomBar убран – он находится в MainActivity
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FDF5))
                .padding(innerPadding)
        ) {

            // ─── Коллекции ──────────────────────────────────────
            CollectionsSection(
                onClick = { navController.navigate(Screen.Collections.route) }
            )

            Spacer(Modifier.height(16.dp))

            // ─── Сетка книг (заглушка) ─────────────────────────
            BooksGrid()
        }
    }
}

/* ────────────────────────────────────────────────────────────
   UI-блоки экрана
   ────────────────────────────────────────────────────────── */

@Composable
private fun CollectionsSection(onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF8FDF5))
    ) {
        Divider(color = Color.LightGray, thickness = 1.dp)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Filled.Menu,
                contentDescription = "Коллекции",
                tint = Color(0xFF2C3E50)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Коллекции",
                fontSize = 16.sp,
                color = Color(0xFF2C3E50),
                modifier = Modifier.weight(1f)
            )
            Icon(
                Icons.Filled.KeyboardArrowRight,
                contentDescription = "Перейти",
                tint = Color(0xFF2C3E50)
            )
        }

        Divider(color = Color.LightGray, thickness = 1.dp)
    }
}

@Composable
private fun BooksGrid() {
    val books = List(10) { idx ->
        Book(title = "Название ${idx + 1}", progress = "${(idx + 1) * 10}%")
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(books) { book -> BookCard(book) }
    }
}

@Composable
private fun BookCard(book: Book) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.7f)
                .background(book.coverColor, RoundedCornerShape(8.dp))
        )
        Spacer(Modifier.height(8.dp))
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
    }
}

/* ────────────────────────────────────────────────────────────
   Preview (только для дизайна, в рантайме вы используете NavHost)
   ────────────────────────────────────────────────────────── */
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LibraryScreenPreview() {
    val navController = rememberNavController()
    LibraryScreen(navController)
}
