package com.example.litera.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons          // ← все иконки пачкой
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
import com.example.litera.ui.AuthButton          // теперь с параметром enabled
import com.example.litera.ui.BackgroundColor
import com.example.litera.ui.PrimaryRed
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(navController: NavController) {

    /* UI-состояние */
    var email     by remember { mutableStateOf("") }
    var password  by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showPass  by remember { mutableStateOf(false) }

    /* Firebase */
    val auth              = remember { FirebaseAuth.getInstance() }
    val scope             = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
            Spacer(Modifier.height(72.dp))

            /* --- E-mail --- */
            LiteraTextField(
                value = email,
                onValueChange = { email = it },
                label = "E-mail",
                keyboardType = KeyboardType.Email
            )

            Spacer(Modifier.height(16.dp))

            /* --- Пароль --- */
            LiteraTextField(
                value = password,
                onValueChange = { password = it },
                label = "Пароль",
                isPassword = !showPass,
                trailingIcon = {
                    IconButton(onClick = { showPass = !showPass }) {
                        Icon(
                            imageVector = if (showPass) Icons.Default.VisibilityOff
                            else Icons.Default.Visibility,
                            contentDescription = null,
                            tint = PrimaryRed
                        )
                    }
                }
            )

            Spacer(Modifier.weight(1f))

            /* --- Кнопка входа --- */
            AuthButton(
                text = if (isLoading) "Подождите…" else "Войти",
                enabled = !isLoading && email.isNotBlank() && password.isNotBlank()
            ) {
                isLoading = true
                auth.signInWithEmailAndPassword(email.trim(), password.trim())
                    .addOnCompleteListener { task ->
                        isLoading = false
                        if (task.isSuccessful) {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.First.route) { inclusive = true }
                            }
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    task.exception?.localizedMessage ?: "Ошибка входа"
                                )
                            }
                        }
                    }
            }

            Spacer(Modifier.weight(0.7f))
        }
    }
}

/* универсальный TextField в фирменных цветах */
@Composable
private fun LiteraTextField(
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
@Preview(showBackground = true)
@Composable
private fun AuthScreenPreview() {
    AuthScreen(rememberNavController())
}
