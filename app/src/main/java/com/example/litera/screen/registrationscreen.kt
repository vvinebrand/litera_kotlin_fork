/**
 * RegistrationScreen.kt
 * Создание учётной записи в Firebase Auth + запись логина в Firestore.
 * При успехе → Home-экран, Welcome стирается из back-stack.
 */

package com.example.litera.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.litera.R
import com.example.litera.navigation.Screen
import com.example.litera.ui.AuthButton
import com.example.litera.ui.BackgroundColor
import com.example.litera.ui.PrimaryRed
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(navController: NavController) {

    /* --- UI-state --- */
    var login     by remember { mutableStateOf("") }
    var email     by remember { mutableStateOf("") }
    var password  by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showPass  by remember { mutableStateOf(false) }

    /* Firebase */
    val auth      = remember { FirebaseAuth.getInstance() }
    val db        = remember { FirebaseFirestore.getInstance() }
    val scope     = rememberCoroutineScope()
    val snack     = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snack) },
        containerColor = BackgroundColor
    ) { inner ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(horizontal = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.weight(1f))
            Image(painterResource(R.drawable.logo), null, Modifier.size(80.dp))
            Spacer(Modifier.height(60.dp))

            /* --- поля ввода --- */
            LiteraField(value = login,    onValueChange = { login    = it }, label = "Логин")
            Spacer(Modifier.height(16.dp))
            LiteraField(value = email,    onValueChange = { email    = it }, label = "E-mail",
                keyboardType = KeyboardType.Email)
            Spacer(Modifier.height(16.dp))
            LiteraField(
                value = password,
                onValueChange = { password = it },
                label = "Пароль",
                isPassword = !showPass,
                trailingIcon = {
                    IconButton(onClick = { showPass = !showPass }) {
                        Icon(
                            imageVector = if (showPass) Icons.Filled.VisibilityOff
                            else Icons.Filled.Visibility,
                            contentDescription = null,
                            tint = PrimaryRed
                        )
                    }
                }
            )

            Spacer(Modifier.weight(1f))

            /* --- кнопка --- */
            AuthButton(
                text     = if (isLoading) "Подождите…" else "Регистрация",
                enabled  = !isLoading && login.isNotBlank()
                        && email.isNotBlank() && password.isNotBlank()
            ) {
                isLoading = true
                auth.createUserWithEmailAndPassword(email.trim(), password.trim())
                    .addOnCompleteListener { task ->
                        isLoading = false
                        if (task.isSuccessful) {
                            /* 1. записываем displayName */
                            task.result?.user?.updateProfile(
                                UserProfileChangeRequest.Builder()
                                    .setDisplayName(login.trim())
                                    .build()
                            )
                            /* 2. сохраняем профиль в Firestore */
                            val uid = task.result?.user?.uid ?: ""
                            db.collection("users").document(uid)
                                .set(mapOf("login" to login.trim(),
                                    "email" to email.trim()))
                            /* 3. переходим на Home */
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.First.route) { inclusive = true }
                            }
                        } else {
                            scope.launch {
                                snack.showSnackbar(
                                    task.exception?.localizedMessage
                                        ?: "Не удалось зарегистрироваться"
                                )
                            }
                        }
                    }
            }

            Spacer(Modifier.weight(0.7f))
        }
    }
}

/* универсальный фирменный TextField */
@Composable
private fun LiteraField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        visualTransformation =
            if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        trailingIcon = trailingIcon,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor   = PrimaryRed,
            unfocusedBorderColor = PrimaryRed,
            focusedLabelColor    = PrimaryRed,
            cursorColor          = PrimaryRed
        )
    )
}

/* Preview */
@Preview(showBackground = true, device = "id:pixel_6")
@Composable
private fun RegistrationScreenPreview() {
    RegistrationScreen(rememberNavController())
}
