/**
 * HomeScreen.kt
 * Стартовый экран приложения: текущая книга + «Недавние».
 *  • Не содержит BottomBar – она задаётся в MainActivity.
 *  • NavController не нужен, потому что переходов с этого экрана пока нет.
 */

package com.example.litera.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController // только для превью

/* ──────────────────────────────────────────────────────────── */

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FDF5))
            .padding(16.dp)
    ) {
        CurrentBookCard()

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Недавние",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(12.dp))

        RecentBooksRow()
    }
}

/* ─── UI-блоки ────────────────────────────────────────────── */

@Composable
private fun CurrentBookCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(Color.LightGray, RoundedCornerShape(8.dp))
            )

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text("Название книги", fontWeight = FontWeight.Bold)
                Text("Автор", fontSize = 12.sp, color = Color.Gray)
                Spacer(Modifier.height(8.dp))
                Text("67%", fontSize = 12.sp, color = Color.Gray)
            }

            IconButton(onClick = { /* TODO: Открыть книгу */ }) {
                Icon(
                    Icons.Default.ArrowForward,
                    contentDescription = "Перейти",
                    tint = Color.Red
                )
            }
        }
    }
}

@Composable
private fun RecentBooksRow() {
    val items = List(3) { "Book $it" }

    LazyRow {
        items(items) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(end = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 100.dp, height = 140.dp)
                        .background(Color.LightGray, RoundedCornerShape(8.dp))
                )
                Spacer(Modifier.height(8.dp))
                Text("Название", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Text("Процент", fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}

/* ─── Preview для Android Studio ──────────────────────────── */

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    // NavController не нужен, но для единообразия превью строим из NavHost
    HomeScreen()
}
