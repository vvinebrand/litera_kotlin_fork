package com.example.litera.screen

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.litera.drive.DriveServiceHelper
import com.example.litera.navigation.Screen
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(navController: NavController) {

    /* ---------- Firebase ---------- */
    val auth = remember { FirebaseAuth.getInstance() }
    var userName by remember { mutableStateOf("…") }

    DisposableEffect(Unit) {
        val l = FirebaseAuth.AuthStateListener { fa ->
            userName = fa.currentUser?.displayName ?: fa.currentUser?.email ?: "Гость"
        }
        l.onAuthStateChanged(auth)
        auth.addAuthStateListener(l)
        onDispose { auth.removeAuthStateListener(l) }
    }

    /* ---------- Google Drive sign-in ---------- */
    val ctx       = LocalContext.current
    val snack     = remember { SnackbarHostState() }
    val scope     = rememberCoroutineScope()

    val gDriveLauncher =
        rememberLauncherForActivityResult(StartActivityForResult()) { res ->

            // 1. Пытаемся извлечь аккаунт из интента
            val task = GoogleSignIn.getSignedInAccountFromIntent(res.data)

            try {
                val account = task.getResult(ApiException::class.java)

                // 2. Успех – аккаунт сохранён в Play-Services
                scope.launch {
                    snack.showSnackbar("Google Drive подключён: ${account.email}")
                }

            } catch (e: ApiException) {
                // 3. Ошибка или пользователь нажал «Назад»
                scope.launch {
                    snack.showSnackbar("Вход не удался (код ${e.statusCode})")
                }
            }
        }

    Scaffold(
        snackbarHost = { SnackbarHost(snack) },
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Профиль", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C3E50))
                },
                actions = {
                    /* Кнопка синхронизации Google Drive */
                    IconButton(
                        onClick = {
                            val intent = DriveServiceHelper.signInClient(ctx).signInIntent
                            gDriveLauncher.launch(intent)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CloudSync,
                            contentDescription = "Google Drive",
                            tint = Color(0xFF2C3E50)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { inner ->

        Column(
            Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FDF5))
                .padding(inner)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(24.dp))

            Box(
                Modifier.size(100.dp).background(Color.LightGray, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, null, Modifier.size(60.dp), tint = Color.Gray)
            }

            Spacer(Modifier.height(8.dp))

            Text(text = userName, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C3E50))

            Spacer(Modifier.height(32.dp))

            AlignStartText("Статистика", 24.sp, FontWeight.Bold)
            Spacer(Modifier.height(16.dp))

            StatisticCard("3",   "книги", "прочитано")
            Spacer(Modifier.height(12.dp))
            StatisticCard("927", "стр",   "прочитано")
            Spacer(Modifier.height(12.dp))
            StatisticCard("3ч57мин", "",  "время за чтением")

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = {
                    auth.signOut()
                    navController.navigate(Screen.First.route) {
                        popUpTo(Screen.First.route) { inclusive = true }
                    }
                    scope.launch { snack.showSnackbar("Вы вышли из аккаунта") }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))
            ) {
                Text(text ="Выйти", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

/* ---------- вспомогательные Composable ---------- */

@Composable
private fun AlignStartText(text: String, fontSize: TextUnit, fontWeight: FontWeight) {
    Text(
        text = text,
        fontSize = fontSize,
        fontWeight = fontWeight,
        color = Color(0xFF2C3E50),
        modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.Start)
    )
}

@Composable
private fun StatisticCard(value: String, unit: String, description: String) {
    Card(
        Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(text = value, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.Red)
                    if (unit.isNotBlank()) {
                        Spacer(Modifier.width(4.dp))
                        Text(text = unit, fontSize = 16.sp, color = Color.Red, modifier = Modifier.padding(bottom = 2.dp))
                    }
                }
                Text(text = description, fontSize = 14.sp, color = Color.Gray)
            }
            Box(
                Modifier.size(40.dp).background(Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AccountPreview() {
    AccountScreen(rememberNavController())
}
