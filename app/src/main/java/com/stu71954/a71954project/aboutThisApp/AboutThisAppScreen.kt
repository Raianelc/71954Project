package com.stu71954.a71954project.aboutThisApp

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.wear.compose.material3.Icon
import com.stu71954.a71954project.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutThisAppScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF154670),
                    titleContentColor = colors.onPrimary
                ),
                title = { Text("About This App") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFFFFFFFF)
                        )
                    }
                }
            )
        },
        content = { innerPadding ->

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,

            ) {
                Text(text = "App Version: 1.0.0", fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                Text(text = "Developed by: Raiane Lopes", fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                Text(text = "Contact: raianelopes88@gmail.com", fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                SocialMediaIcons()
                Spacer(modifier = Modifier.padding(10.dp))
                Text(
                    modifier = Modifier.padding(5.dp),
                    text = "This app was developed as a project for the Mobile App Development course at Dorset College.",
                    fontSize = 17.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold)
                Button(
                    colors = buttonColors(containerColor = Color(0xFFEF8121)),
                    onClick = { navController.navigate("productlistscreen") }
                ) {
                    Text("Go to Home")
                }
            }
        }
    )
}

@Composable
fun SocialMediaIcons() {
    val context = LocalContext.current
    val openUrlLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { }

    Row {
        IconButton(onClick = {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://github.com/Raianelc")
            openUrlLauncher.launch(openURL)
        }) {
            Icon(
                modifier = Modifier.size(60.dp),
                tint = Color(0xFF030303),
                painter = painterResource(R.drawable.github), contentDescription = "GitHub Icon")
        }

        IconButton(onClick = {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://www.linkedin.com/in/raianelopesc/")
            openUrlLauncher.launch(openURL)
        }) {
            Icon(
                modifier = Modifier.size(60.dp),
                tint = Color(0xFF030303),
                painter = painterResource(R.drawable.icons8_linkedin), contentDescription = "LinkedIn Icon")
        }
    }
}

