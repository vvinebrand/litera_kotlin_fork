package com.example.litera.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close // Иконка закрытия (X)
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Для этого экрана не нужны BottomNavigationBar и BottomNavItem,
// так как у него свой уникальный нижний бар для прогресса чтения.

@OptIn(ExperimentalMaterial3Api::class) // Нужен для Scaffold и TopAppBar
@Composable
fun OpenBookScreen() {
    val scrollState = rememberScrollState() // Для прокрутки содержимого книги

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "14 с. до конца главы", // Текст из макета
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.DarkGray // Цвет, похожий на макет
                    )
                },
                actions = {
                    IconButton(onClick = { /* TODO: Логика закрытия книги */ }) {
                        Icon(
                            Icons.Default.Close, // Иконка X
                            contentDescription = "Закрыть книгу",
                            tint = Color.DarkGray // Цвет, похожий на макет
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White // Фон верхней панели
                )
            )
        },
        bottomBar = {
            BottomAppBar( // Используем BottomAppBar для прогресса чтения
                containerColor = Color.White,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "60 из 962", // Прогресс чтения из макета
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.DarkGray
                    )
                    // Здесь может быть иконка или другой элемент,
                    // если он подразумевается в правой части нижнего бара.
                    // Например, Icons.Default.Settings для настроек чтения,
                    // или просто пустой Box, если ничего не нужно.
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(Color.Transparent) // Заглушка, если ничего нет
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FDF5)) // Светлый фон, как в других экранах
                .padding(innerPadding) // Отступы от TopAppBar/BottomBar
                .padding(horizontal = 16.dp) // Горизонтальные отступы для текста книги
                .verticalScroll(scrollState) // Делаем контент прокручиваемым
        ) {
            // Заглушка текста книги, имитирующая абзацы
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Она шла по узкой, вымощенной камнем улице, где фонари едва освещали дорогу, отбрасывая длинные тени на старые кирпичные стены. В воздухе висел запах дождя, смешанный с ароматом влажной земли и далёких костров. Её шаги были осторожными, но уверенными – каждый шаг был шагом в прошлое, в историю, которую она пыталась забыть.\n\n" +
                        "В тот вечер город жил своей привычной жизнью: кто-то спешил домой, кто-то договаривался о сделке, кто-то тихо смеялся в полутёмных переулках. Она же шла дальше, стараясь не думать о том, что ждёт её впереди.\n\n" +
                        "Ещё в детстве она часто слышала истории о местах, подобных этому – местах, где стены помнят слишком многое. Говорили, что в камнях, сложенных века назад, осталось эхо голосов, разговоров, секретов. И сейчас, проходя мимо высоких фасадов, она чувствовала, как невидимая паутина прошлого тянется к ней, обволакивая лёгким, но ощутимым холодом.\n\n" +
                        "Она остановилась перед старой дверью. Рука сама потянулась к ручке, но она замерла, не решаясь сделать последний шаг. Что если за этой дверью её ждёт не ответ, а только ещё больше вопросов?\n\n" +
                        "Вдох. Выдох.\n\n" +
                        "Дверь скрипнула, открывая перед ней мрак. Внутри было темно, лишь в дальнем углу мерцал слабый свет свечи. Стены здесь были обшарпаны, пол местами скрипел, но всё же место казалось… живым. Она сделала шаг вперёд, касаясь пальцами холодного дерева перил.\n\n" +
                        "– Ты всё-таки пришла, – раздался голос из темноты.\n\n" +
                        "Она узнала его мгновенно.",
                fontSize = 18.sp,
                lineHeight = 26.sp, // Улучшает читабельность длинного текста
                color = Color.Black // Цвет текста книги
            )
            Spacer(modifier = Modifier.height(16.dp)) // Отступ снизу для прокрутки
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OpenBookScreenPreview() {
    OpenBookScreen()
}