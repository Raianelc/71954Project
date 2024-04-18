package com.stu71954.a71954project.userProfile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.stu71954.a71954project.appViewModel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreenProfile(userId: String, viewModel: AppViewModel, navController: NavController) {
    val userDetails by viewModel.user.collectAsState(null)

    LaunchedEffect(userId) {
        viewModel.getUserById(userId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF154670),
                    titleContentColor = colors.onPrimary
                ),
                title = { Text("User Profile") }
            )
        },
        content = { innerPadding ->
            val modifier = Modifier.padding(innerPadding)

            Column(
                modifier = modifier,
                horizontalAlignment = CenterHorizontally,
            ) {
                Image(
                    modifier = Modifier.fillMaxWidth(),
                    painter = rememberImagePainter(userDetails?.picture), contentDescription = "Profile Picture")
                Box ( modifier = Modifier.fillMaxWidth().padding(16.dp),
                ) {
                    Column {
                        Text(text = "First Name: ${userDetails?.firstName}", fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                        Text(text = "Last Name: ${userDetails?.lastName}", fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                        Text(text = "Email: ${userDetails?.email}", fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                        Text(text = "Phone Number: ${userDetails?.phone}", fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                        Text(text = "Address: ${userDetails?.address}", fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
                Button(
                    colors = buttonColors(containerColor = Color(0xFFEF8121)),
                    onClick = { navController.navigate("aboutThisApp") }
                ) {
                    Text("About this App")
                }
            }
        }
    )
}

//use material3