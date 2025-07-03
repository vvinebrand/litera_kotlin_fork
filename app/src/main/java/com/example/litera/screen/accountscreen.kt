/**
 * AccountScreen.kt
 *
 * Экран профиля:
 *  • Больше не хранит `selectedItemIndex` и не рисует свой BottomBar —
 *    общий бар живёт в MainActivity/NavHost.
 *  • Пока никуда не переходит: NavController не требуется. Если позже
 *    появятся «Настройки» / «Детали книги», просто добавьте параметр
 *    `navController: NavController` и вызывайте `navController.navigate(...)`.
 */

package com.example.litera.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

/* ──────────────────────────────────────────────────────────── */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen() {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Профиль",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C3E50)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
        // bottomBar нет – его рисует MainActivity
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FDF5))
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(24.dp))

            /* Аватар + логин */
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.LightGray, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Аватар",
                    modifier = Modifier.size(60.dp),
                    tint = Color.Gray
                )
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Логин",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2C3E50)
            )

            Spacer(Modifier.height(32.dp))

            AlignStartText("Статистика", 24.sp, FontWeight.Bold)
            Spacer(Modifier.height(16.dp))

            /* Карточки статистики */
            StatisticCard("3",   "книги", "прочитано")
            Spacer(Modifier.height(12.dp))
            StatisticCard("927", "стр",   "прочитано")
            Spacer(Modifier.height(12.dp))
            StatisticCard("3ч57мин", "",  "время за чтением")

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = { /* TODO: Logout */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))
            ) {
                Text(
                    text = "Выйти",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

/* ─── Вспомогательные компоненты ─────────────────────────── */

@Composable
private fun AlignStartText(text: String, fontSize: androidx.compose.ui.unit.TextUnit, fontWeight: FontWeight) {
    Text(
        text = text,
        fontSize = fontSize,
        fontWeight = fontWeight,
        color = Color(0xFF2C3E50),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.Start)
    )
}

@Composable
private fun StatisticCard(value: String, unit: String, description: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = value,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    )
                    if (unit.isNotBlank()) {
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = unit,
                            fontSize = 16.sp,
                            color = Color.Red,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }
                }
                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            )
        }
    }
}

/* ─── Preview ─────────────────────────────────────────────── */

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AccountScreenPreview() {
    AccountScreen()
}
