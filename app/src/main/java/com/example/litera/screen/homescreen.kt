package com.example.litera.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
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
    // Если BottomNavItem находится в этом же пакете


@Composable
fun HomeScreen() {
    var selectedItemIndex by remember { mutableStateOf(0) }

    Scaffold(
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
                .padding(16.dp)
                .padding(innerPadding)
        ) {
            CurrentBookCard()
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Недавние",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            RecentBooksRow()
        }
    }
}

@Composable
fun CurrentBookCard() {
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
                    .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Название книги", fontWeight = FontWeight.Bold)
                Text("Автор", fontSize = 12.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Text("67%", fontSize = 12.sp, color = Color.Gray)
            }
            IconButton(onClick = { }) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Перейти", tint = Color.Red)
            }
        }
    }
}

@Composable
fun RecentBooksRow() {
    val items = List(3) { "Book $it" }

    LazyRow {
        items(items) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(end = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp, 140.dp)
                        .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Название", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Text("Процент", fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}

// <-- УДАЛИТЕ ЭТУ ФУНКЦИЮ ИЗ ЭТОГО ФАЙЛА, если она уже есть в LibraryScreen.kt
// @Composable
// fun BottomNavigationBar(selectedItemIndex: Int, onItemSelected: (Int) -> Unit) {
//     NavigationBar(containerColor = Color.White) {
//         // ... (содержимое функции) ...
//     }
// }

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}