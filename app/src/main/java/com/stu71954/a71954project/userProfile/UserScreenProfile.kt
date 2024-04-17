package com.stu71954.a71954project.userProfile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.stu71954.a71954project.appViewModel.AppViewModel

@Composable
fun UserScreenProfile(userId: String, viewModel: AppViewModel, navController: NavController) {
    val userDetails by viewModel.user.collectAsState(null)

    LaunchedEffect(userId) {
        viewModel.getUserById(userId)
    }

    Column {
        Text(text = "First Name: ${userDetails?.firstName}")
        Text(text = "Last Name: ${userDetails?.lastName}")
        Text(text = "Email: ${userDetails?.email}")
        Text(text = "Phone Number: ${userDetails?.phone}")
        Text(text = "Address: ${userDetails?.address}")
        Image(painter = rememberImagePainter(userDetails?.picture), contentDescription = "Profile Picture")
        TextButton(onClick = {navController.navigate("aboutThisApp") }) {
            Text("About this App")

        }
    }


}


//use material3