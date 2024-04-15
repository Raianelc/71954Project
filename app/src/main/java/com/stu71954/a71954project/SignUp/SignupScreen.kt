package com.stu71954.a71954project.SignUp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.stu71954.a71954project.Auth.AuthViewModel
import com.stu71954.a71954project.Auth.AuthViewModelFactory

@Composable
fun SignUpScreen(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val sharedPreferences = context.getSharedPreferences("com.stu71954.a71954project.PREF", android.content.Context.MODE_PRIVATE)
    val viewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(auth, sharedPreferences))

    LaunchedEffect(viewModel.errorMessage) {
        viewModel.errorMessage.observeForever { errorMessage = it }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") }
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") }
        )

        Button(onClick = { viewModel.signup(username, password, navController) }) {
            Text("Sign Up")
        }
        Button(onClick = { navController.navigate("login") }) {
            Text("Login")

        }

        errorMessage?.let { errorMessage ->
            Text(errorMessage)
        }
    }
}