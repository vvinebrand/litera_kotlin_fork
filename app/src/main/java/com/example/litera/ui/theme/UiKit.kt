package com.example.litera.ui       // выбери любой общий пакет

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

/*  ───── Цвета, которые нужны в разных экранах ───── */
val BackgroundColor = Color(0xFFF5FBF6)
val PrimaryRed      = Color(0xFFEA4335)

/*  ───── Переиспользуемая кнопка-авторизации ───── */
/* UiKit.kt */
@Composable
fun AuthButton(
    text: String,
    enabled: Boolean = true,          // ← добавили
    onClick: () -> Unit
) {
    Button(
        enabled = enabled,
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = PrimaryRed,
            contentColor   = Color.White
        )
    ) {
        Text(text =  text, fontSize =  18.sp, fontWeight = FontWeight.Medium)
    }
}

