/**
 * FirstScreen.kt
 * Welcome-экран: показывает логотип + две кнопки.
 *   • «Вход»  →  Screen.Auth
 *   • «Регистрация» → Screen.Register
 */

package com.example.litera.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.litera.R
import com.example.litera.navigation.Screen
import com.example.litera.ui.AuthButton       // из UiKit.kt
import com.example.litera.ui.BackgroundColor // из UiKit.kt

@Composable
fun FirstScreen(navController: NavController) {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = BackgroundColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            /* логотип */
            Spacer(Modifier.weight(1f))

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Логотип Litera",
                modifier = Modifier.size(100.dp)
            )

            Spacer(Modifier.height(12.dp))
            Spacer(Modifier.weight(1.5f))

            /* кнопка «Вход» */
            AuthButton("Вход") {
                navController.navigate(Screen.Auth.route) {
                    popUpTo(Screen.First.route) { inclusive = true }
                }
            }

            Spacer(Modifier.height(16.dp))

            /* кнопка «Регистрация» */
            AuthButton("Регистрация") {
                navController.navigate(Screen.Register.route) {
                    popUpTo(Screen.First.route) { inclusive = true }
                }
            }

            Spacer(Modifier.weight(0.5f))
        }
    }
}

/* Preview — создаём временный NavController */
@Preview(showBackground = true)
@Composable
private fun FirstScreenPreview() {
    FirstScreen(rememberNavController())
}
