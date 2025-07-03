package com.example.litera.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.litera.R
import com.example.litera.navigation.Screen

private val backgroundColor = Color(0xFFF5FBF6)
private val primaryRed      = Color(0xFFEA4335)

@Composable
fun FirstScreen(navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(Modifier.weight(1f))          // ↑ сдвигает логотип чуть выше центра

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Логотип Litera",
                modifier = Modifier.size(100.dp)
            )

            Spacer(Modifier.height(12.dp))
            Spacer(Modifier.weight(1.5f))        // ↓ оставляет место для кнопок

            AuthButton("Вход") {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.First.route) { inclusive = true }
                }
            }

            Spacer(Modifier.height(16.dp))

            AuthButton("Регистрация") {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.First.route) { inclusive = true }
                }
            }

            Spacer(Modifier.weight(0.5f))
        }
    }
}

@Composable
private fun AuthButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = primaryRed,
            contentColor   = Color.White
        )
    ) { Text(text = text, fontSize = 18.sp, fontWeight = FontWeight.Medium) }
}

/* Preview без навигации */
@Preview(showBackground = true)
@Composable
private fun FirstScreenPreview() {
    FirstScreen(navController = rememberNavController())
}
