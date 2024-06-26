package com.stu71954.a71954project.signUp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.stu71954.a71954project.auth.AuthViewModel
import com.stu71954.a71954project.auth.AuthViewModelFactory

@Composable
fun SignUpScreen(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val sharedPreferences = context.getSharedPreferences(
        "com.stu71954.a71954project.PREF",
        android.content.Context.MODE_PRIVATE
    )
    val viewModel: AuthViewModel =
        viewModel(factory = AuthViewModelFactory(auth, sharedPreferences))

    LaunchedEffect(viewModel.errorMessage) {
        viewModel.errorMessage.observeForever { errorMessage = it }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center // Adjusted vertical arrangement
    ) {
        Image(
            painter = painterResource(id = com.stu71954.a71954project.R.drawable.dark_blue_and_orange_online_shop_logo),
            contentDescription = "Logo",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 16.dp) // Adjusted padding
        )

        Text(
            text = "SIGN UP",
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            fontSize = 24.sp,
            color = Color(0xFF1C486F),
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
        )
        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { viewModel.signup(username, password, navController) },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF1C486F), contentColor = Color.White)

            ) {
                Text("Sign Up")
            }
            // Added space between buttons
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = { navController.navigate("login") },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF1C486F), contentColor = Color.White)
            ) {
                Text("Login")
            }
        }
        errorMessage?.let { errorMessage ->
            Text(
                errorMessage,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 8.dp) // Adjusted padding
            )
        }
    }
}