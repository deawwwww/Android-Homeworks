package com.example.hw1.model

import kotlinx.serialization.Serializable

class Routes {
    @Serializable
    data class AuthRouteModel(
        val generatedPassword: String
    )

    @Serializable
    data class ResultRouteModel(
        val userData: UserDataModel,
        val generatedPassword: String
    )
}