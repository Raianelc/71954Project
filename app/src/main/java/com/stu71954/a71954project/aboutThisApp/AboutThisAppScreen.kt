package com.stu71954.a71954project.aboutThisApp

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun AboutThisAppScreen( navController: NavController) {
    Column {
        Text(text = "This is a simple shopping app that allows users to view products, add them to their cart, and place orders.")
        Text(text = "The app is built using Jetpack Compose and Firebase.")
        Text(text = "The app is built by Stu71954.")
    }
}