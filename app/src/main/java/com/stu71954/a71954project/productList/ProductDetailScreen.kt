package com.stu71954.a71954project.productList

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.stu71954.a71954project.appViewModel.AppViewModel
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import com.stu71954.a71954project.auth.AuthViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter

@Composable
fun ProductDetailScreen(productId: Int, viewModel: AppViewModel, navController: NavController, authViewModel: AuthViewModel) {
    viewModel.fetchProductById(productId)
    val productState by viewModel.product.collectAsState()

    productState?.let { product ->
        var quantity by remember { mutableStateOf(1) }
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = product.title) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            },
            bottomBar = {
                BottomAppBar {
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ){
                        Box {
                            Row (
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ){
                                Button(onClick = { if (quantity > 1) quantity-- }) {
                                    Text("-")
                                }
                                Text("$quantity")
                                Button(onClick = { quantity++ }) {
                                    Text("+")
                                }
                            }
                        }
                        Button(onClick = { /* handle buy action */ }) {
                            Text("Buy")
                        }
                    }
                }
            }
        ) { innerPadding ->
            Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(innerPadding)) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = rememberImagePainter(product.image),
                        contentDescription = product.title,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Surface (
                        modifier = Modifier.padding(8.dp),
                        elevation = 2.dp

                    ) {
                        Text(text = product.description, fontSize = 18.sp, modifier = Modifier.padding(8.dp))
                    }
                    Text(text = "Price: ${product.price}")
                }
            }
        }
    } ?: run {
        // handle case where product is null, e.g. loading or error state
    }
}