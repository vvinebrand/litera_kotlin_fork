package com.example.litera.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person // Иконка для профиля
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

@OptIn(ExperimentalMaterial3Api::class) // Нужен для Scaffold и TopAppBar
@Composable
fun AccountScreen() {
    // Состояние для отслеживания выбранного пункта навигации
    // Индекс 3 соответствует "Профиль" в вашем BottomNavigationBar (Главная=0, Библиотека=1, Поиск=2, Профиль=3)
    var selectedItemIndex by remember { mutableStateOf(3) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Профиль", // Можно оставить "Профиль" или убрать, если дизайн его не предусматривает
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C3E50)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White // Фон верхней панели, как в других экранах
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
                .background(Color(0xFFF8FDF5)) // Светлый фон из ваших примеров
                .padding(innerPadding)
                .padding(horizontal = 16.dp), // Горизонтальные отступы для контента
            horizontalAlignment = Alignment.CenterHorizontally // Центрируем элементы по горизонтали
        ) {
            Spacer(modifier = Modifier.height(24.dp)) // Отступ сверху

            // Блок Аватара и Логина
            Box(
                modifier = Modifier
                    .size(100.dp) // Размер заглушки аватара
                    .background(Color.LightGray, shape = CircleShape), // Круглая форма и серый цвет
                contentAlignment = Alignment.Center
            ) {
                // Здесь может быть иконка или изображение аватара
                Icon(
                    Icons.Default.Person, // Пример иконки, если нет изображения
                    contentDescription = "Аватар пользователя",
                    modifier = Modifier.size(60.dp),
                    tint = Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Логин", // Текст "Логин"
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2C3E50)
            )
            Spacer(modifier = Modifier.height(32.dp)) // Отступ до статистики

            // Заголовок "Статистика"
            AlignStartText(text = "Статистика", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            // Карточки статистики
            StatisticCard(
                value = "3",
                unit = "книги",
                description = "прочитано"
            )
            Spacer(modifier = Modifier.height(12.dp)) // Отступ между карточками
            StatisticCard(
                value = "927",
                unit = "стр",
                description = "прочитано"
            )
            Spacer(modifier = Modifier.height(12.dp))
            StatisticCard(
                value = "3ч57мин",
                unit = "", // Нет единицы, так как "часы и минуты" уже в значении
                description = "время за чтением"
            )
            Spacer(modifier = Modifier.height(32.dp)) // Отступ до кнопки "Выйти"

            // Кнопка "Выйти"
            Button(
                onClick = { /* TODO: Действие для выхода из аккаунта */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp), // Скругленные углы
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)) // Красный цвет кнопки
            ) {
                Text(
                    text = "Выйти", // Текст кнопки
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(16.dp)) // Отступ снизу
        }
    }
}

@Composable
fun AlignStartText(text: String, fontSize: androidx.compose.ui.unit.TextUnit, fontWeight: FontWeight) {
    Text(
        text = text,
        fontSize = fontSize,
        fontWeight = fontWeight,
        color = Color(0xFF2C3E50),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.Start) // Выравнивание по левому краю
    )
}

@Composable
fun StatisticCard(value: String, unit: String, description: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp), // Скругленные углы карточек
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp) // Небольшая тень
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
                        text = value, // Значение статистики (например, "3", "927", "3ч57мин")
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Red // Красный цвет значения
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = unit, // Единица измерения (например, "книги", "стр")
                        fontSize = 16.sp,
                        color = Color.Red, // Красный цвет единицы
                        modifier = Modifier.padding(bottom = 2.dp) // Немного опустить, чтобы выровнять по нижней линии
                    )
                }
                Text(
                    text = description, // Описание (например, "прочитано", "время за чтением")
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
            // Пустая заглушка справа, как на макете
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.LightGray.copy(alpha = 0.3f), shape = RoundedCornerShape(8.dp))
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AccountScreenPreview() {
    AccountScreen()
}