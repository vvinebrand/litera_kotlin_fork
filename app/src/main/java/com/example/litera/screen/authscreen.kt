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
import com.example.litera.R

@Composable
fun AuthScreen() {
    // Состояния для хранения введенных данных в полях
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundColor
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
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Логотип Litera",
                modifier = Modifier.size(80.dp) // Логотип чуть меньше
            )

            Spacer(modifier = Modifier.height(12.dp))

            Spacer(modifier = Modifier.height(60.dp)) // Большой отступ до полей ввода

            // Поля для ввода логина и пароля
            CustomOutlinedTextField(
                value = login,
                onValueChange = { login = it },
                label = "Логин"
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomOutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = "Пароль",
                isPassword = true
            )

            Spacer(modifier = Modifier.weight(1f)) // Гибкий отступ, чтобы кнопка была ниже

            // Кнопка входа
            AuthButton(
                text = "Вход",
                onClick = {
                    // TODO: Логика аутентификации
                    // println("Login: $login, Password: $password")
                }
            )

            Spacer(modifier = Modifier.weight(0.7f)) // Отступ снизу
        }
    }
}

// Переиспользуемый OutlinedTextField
@Composable
fun CustomOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false
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
            keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Text
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = primaryRed,    // Цвет рамки в фокусе
            unfocusedBorderColor = primaryRed,  // Цвет рамки не в фокусе
            focusedLabelColor = primaryRed,     // Цвет лейбла в фокусе
            cursorColor = primaryRed            // Цвет курсора
        )
    )
}

@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun AuthScreenPreview() {
    AuthScreen()
}