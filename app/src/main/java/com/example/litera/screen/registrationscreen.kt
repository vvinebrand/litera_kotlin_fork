package com.example.litera.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.litera.R // Убедитесь, что импорт R правильный

// Переиспользуем те же цвета, что и на других экранах
// (убедитесь, что эти val определены один раз в вашем проекте, например, в отдельном файле Theme.kt или Constants.kt)
// val backgroundColor = Color(0xFFF5FBF6) // Если эти цвета уже определены в другом месте, закомментируйте их здесь, чтобы избежать конфликтов
// val primaryRed = Color(0xFFEA4335)     // Если эти цвета уже определены в другом месте, закомментируйте их здесь, чтобы избежать конфликтов

@Composable
fun RegistrationScreen() {
    // Состояния для хранения введенных данных в полях
    var login by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Используем уже определенные цвета, если они доступны в пакете screen
    // Если backgroundColor и primaryRed определены глобально или в другом файле,
    // вам не нужно их переопределять здесь. Убедитесь, что они доступны для импорта или в текущем scope.
    val localBackgroundColor = Color(0xFFF5FBF6)
    val localPrimaryRed = Color(0xFFEA4335)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = localBackgroundColor // Используем цвет фона
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp), // Горизонтальные отступы
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f)) // Отступ сверху

            // Секция с логотипом
            Image(
                painter = painterResource(id = R.drawable.logo), // Убедитесь, что у вас есть logo.png в папке drawable
                contentDescription = "Логотип Litera",
                modifier = Modifier.size(80.dp) // Логотип чуть меньше
            )

            Spacer(modifier = Modifier.height(12.dp))

            Spacer(modifier = Modifier.height(60.dp)) // Большой отступ до полей ввода

            // Поля для ввода логина, электронной почты и пароля
            CustomOutlinedTextField(
                value = login,
                onValueChange = { login = it },
                label = "Логин",
                primaryColor = localPrimaryRed // Передаем цвет для поля ввода
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomOutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = "Электронная почта",
                keyboardType = KeyboardType.Email, // Указываем тип клавиатуры для email
                primaryColor = localPrimaryRed // Передаем цвет для поля ввода
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomOutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = "Пароль",
                isPassword = true, // Скрывает вводимый текст
                primaryColor = localPrimaryRed // Передаем цвет для поля ввода
            )

            Spacer(modifier = Modifier.weight(1f)) // Гибкий отступ, чтобы кнопка была ниже

            // Кнопка регистрации
            AuthButton(
                text = "Регистрация",
                onClick = {
                    // TODO: Логика регистрации
                    println("Login: $login, Email: $email, Password: $password")
                },
                primaryColor = localPrimaryRed // Передаем цвет для кнопки
            )

            Spacer(modifier = Modifier.weight(0.7f)) // Отступ снизу
        }
    }
}

// Измененная функция CustomOutlinedTextField для приема цвета
@Composable
fun CustomOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text, // Добавлен параметр для типа клавиатуры
    primaryColor: Color // Принимаем цвет
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType // Используем переданный тип клавиатуры
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = primaryColor,    // Цвет рамки в фокусе
            unfocusedBorderColor = primaryColor,  // Цвет рамки не в фокусе
            focusedLabelColor = primaryColor,     // Цвет лейбла в фокусе
            cursorColor = primaryColor            // Цвет курсора
        )
    )
}

// Измененная функция AuthButton для приема цвета
@Composable
fun AuthButton(text: String, onClick: () -> Unit, primaryColor: Color) { // Принимаем цвет
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = primaryColor, // Используем переданный цвет
            contentColor = Color.White
        )
    ) {
        Text(text = text, fontSize = 18.sp, fontWeight = FontWeight.Medium)
    }
}

@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun RegistrationScreenPreview() {
    RegistrationScreen()
}