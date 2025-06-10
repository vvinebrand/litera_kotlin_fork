package com.example.litera.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.litera.R // Убедитесь, что импорт R правильный

// Определяем цвета, чтобы их было легко изменять
val backgroundColor = Color(0xFFF5FBF6) // Светло-зеленоватый фон
val primaryRed = Color(0xFFEA4335)     // Красный для кнопок, текста и лого

@Composable
fun FirstScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp), // Горизонтальные отступы для кнопок
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Секция с логотипом и названием
            // Spacer выталкивает эту секцию немного вверх от центра
            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(id = R.drawable.logo), // Ваш логотип
                contentDescription = "Логотип Litera",
                modifier = Modifier.size(100.dp) // Размер логотипа
            )

            Spacer(modifier = Modifier.height(12.dp)) // Пространство между лого и текстом

            // Динамическое пространство, которое займет все оставшееся место
            // и отодвинет кнопки вниз
            Spacer(modifier = Modifier.weight(1.5f))

            // Секция с кнопками
            AuthButton(text = "Вход", onClick = { /* TODO: Действие для входа */ })

            Spacer(modifier = Modifier.height(16.dp)) // Пространство между кнопками

            AuthButton(text = "Регистрация", onClick = { /* TODO: Действие для регистрации */ })

            // Небольшой отступ снизу
            Spacer(modifier = Modifier.weight(0.5f))
        }
    }
}

// Переиспользуемая Composable-функция для кнопок
@Composable
fun AuthButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth() // Кнопка на всю ширину с учетом padding у Column
            .height(50.dp), // Высота кнопки
        shape = RoundedCornerShape(16.dp), // Скругление углов
        colors = ButtonDefaults.buttonColors(
            containerColor = primaryRed, // Цвет фона кнопки
            contentColor = Color.White    // Цвет текста на кнопке
        )
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

// Функция для предпросмотра в Android Studio
@Preview(showBackground = true)
@Composable
fun FirstScreenPreview() {
    FirstScreen()
}