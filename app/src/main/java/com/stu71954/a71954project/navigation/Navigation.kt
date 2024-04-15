package com.stu71954.a71954project.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.stu71954.a71954project.appRepository.AppRepository
import com.stu71954.a71954project.appViewModel.AppViewModel
import com.stu71954.a71954project.auth.AuthViewModel
import com.stu71954.a71954project.loginUser.LoginScreen
import com.stu71954.a71954project.productList.ProductListScreen
import com.stu71954.a71954project.signUp.SignUpScreen

@Composable
fun Navigation() {
    val auth = FirebaseAuth.getInstance()
    val navController = rememberNavController()

    val sharedPref = LocalContext.current.getSharedPreferences("com.stu71954.a71954project.PREF", android.content.Context.MODE_PRIVATE)

    val appViewModel = AppViewModel(AppRepository(), navController)
    val authViewModel = AuthViewModel(auth, sharedPref)

    val startDestination = "signup"
    NavHost(
        navController = navController,
        startDestination = startDestination)
    {
        composable("signup") { SignUpScreen(navController = navController) }
        composable("login") { LoginScreen(navController = navController) }
        composable("productlistscreen") { ProductListScreen(appViewModel, navController, authViewModel) }


    }
}