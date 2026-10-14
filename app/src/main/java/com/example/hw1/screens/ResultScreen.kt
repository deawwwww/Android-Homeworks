package com.example.hw1.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.hw1.Consts
import com.example.hw1.model.UserDataModel

@Composable
fun ResultScreen(
    userData: UserDataModel,
    generatedPassword: String,
    onBackClick: () -> Unit
) {
    val isPasswordRight = userData.password == generatedPassword
    val imageUrl = if (isPasswordRight) {
        "https://avatars.mds.yandex.net/i?id=c6b6aab12e0d66c134beb7ca056b64b1_l-8261187-images-thumbs&n=13"
    } else {
        "https://cs12.pikabu.ru/post_img/2022/02/07/1/og_og_1644191149238636602.jpg"
    }

    var isPasswordVisible by remember { mutableStateOf(false) }
    val clipboardManager = LocalClipboardManager.current

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(Consts.DEFAULT_PADDING_DP),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "",
                modifier = Modifier
                    .size(180.dp)
                    .padding(bottom = Consts.DEFAULT_PADDING_DP)
            )

            Text(
                text = if (isPasswordRight) "Пароль верный" else "Пароль не верный",
                modifier = Modifier.padding(bottom = Consts.DEFAULT_PADDING_DP)
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = generatedPassword,
                onValueChange = {},
                readOnly = true,
                label = { Text("Пароль") },
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Показать/скрыть пароль"
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(Consts.DEFAULT_PADDING_DP))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    clipboardManager.setText(AnnotatedString(generatedPassword))
                }
            ) {
                Text(text = "Скопировать пароль")
            }

            Spacer(modifier = Modifier.height(Consts.DEFAULT_PADDING_DP))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onBackClick() }
            ) {
                Text(text = "Назад")
            }
        }
    }
}