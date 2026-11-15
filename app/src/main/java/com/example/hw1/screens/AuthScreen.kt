package com.example.hw1.screens

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.hw1.Consts
import com.example.hw1.model.UserDataModel

@Composable
fun AuthRoute(
    generatedPassword: String,
    onAuthSuccess: (UserDataModel) -> Unit
) {
    val emailTextState = rememberSaveable { mutableStateOf("") }
    val emailError = remember { mutableStateOf(false) }

    val passwordTextState = rememberSaveable { mutableStateOf("") }
    val passwordError = remember { mutableStateOf(false) }

    fun authCheck() {
        val email = emailTextState.value.trim()
        val password = passwordTextState.value
        var hasError = false

        val isEmailValid = email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
        if (!isEmailValid) {
            emailError.value = true
            hasError = true
        } else {
            emailError.value = false
        }

        val hasLetter = password.any { it in 'a'..'z' || it in 'A'..'Z' }
        val hasDigit = password.any { it.isDigit() }
        val isPasswordValid = password.length >= 8 && hasLetter && hasDigit

        if (!isPasswordValid) {
            passwordError.value = true
            hasError = true
        } else {
            passwordError.value = false
        }

        if (!hasError) {
            onAuthSuccess(
                UserDataModel(
                    email = email,
                    password = password
                )
            )
        }
    }
    Scaffold(
        containerColor = Color.White
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Consts.DEFAULT_PADDING_DP),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //email
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = emailTextState.value,
                    onValueChange = {
                        emailTextState.value = it
                        emailError.value = false
                    },
                    label = { Text(text = "Введите email") },
                    isError = emailError.value,
                    supportingText = {
                        if (emailError.value) {
                            Text(text = "Некорректный email", color = Color.Red)
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    maxLines = 1
                )
                //password
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = passwordTextState.value,
                    onValueChange = {
                        passwordTextState.value = it
                        passwordError.value = false
                    },
                    label = { Text(text = "Введите пароль") },
                    visualTransformation = PasswordVisualTransformation(),
                    isError = passwordError.value,
                    supportingText = {
                        if (passwordError.value) {
                            Text(text = "Некорректный пароль", color = Color.Red)
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { authCheck() }
                    ),
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { authCheck() }
                ) {
                    Text(text = "Далее")
                }
            }
        }
    }


}

