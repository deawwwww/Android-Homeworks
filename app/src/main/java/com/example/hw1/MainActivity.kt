package com.example.hw1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.hw1.model.UserDataModel
import com.example.hw1.screens.AuthRoute
import kotlin.reflect.typeOf
import com.example.hw1.model.Routes.AuthRouteModel
import com.example.hw1.model.Routes.ResultRouteModel
import com.example.hw1.model.userDataNavType
import com.example.hw1.screens.ResultScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val generatedPassword = generateRandomPassword()

        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = AuthRouteModel(generatedPassword = generatedPassword),
            ) {
                composable<AuthRouteModel> { entry ->
                    val args = entry.toRoute<AuthRouteModel>()
                    AuthRoute(
                        generatedPassword = args.generatedPassword,
                        onAuthSuccess = { userData ->
                            navController.navigate(
                                ResultRouteModel(
                                    userData = userData,
                                    generatedPassword = args.generatedPassword
                                )
                            )
                        }
                    )
                }

                composable<ResultRouteModel>(
                    typeMap = mapOf(typeOf<UserDataModel>() to userDataNavType),
                ) { entry ->
                    val args = entry.toRoute<ResultRouteModel>()
                    ResultScreen(
                        userData = args.userData,
                        generatedPassword = args.generatedPassword,
                        onBackClick = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }

    private fun generateRandomPassword(): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..10).map { chars.random() }.joinToString("")
    }
}