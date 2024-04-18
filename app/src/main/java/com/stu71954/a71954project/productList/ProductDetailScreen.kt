package com.stu71954.a71954project.productList

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomAppBar
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.stu71954.a71954project.appViewModel.AppViewModel
import com.stu71954.a71954project.auth.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(productId: Int, viewModel: AppViewModel, navController: NavController, authViewModel: AuthViewModel) {
    viewModel.fetchProductById(productId)
    val productState by viewModel.product.collectAsState()

    productState?.let { product ->
        var quantity by remember { mutableStateOf(1) }
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF154670),
                        titleContentColor = colors.onPrimary
                    ),
                    title = { Text(text = product.title) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            },
            bottomBar = {
                BottomAppBar(
                    backgroundColor = Color(0xFF154670),
                    contentColor = contentColorFor(MaterialTheme.colors.primary)
                ) {
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
                                Button(
                                    colors = buttonColors(containerColor = Color(0xFFEF8121)),
                                    onClick = { if (quantity > 1) quantity-- }
                                ) {
                                    Text("-", fontSize = 20.sp)
                                }
                                Text(modifier = Modifier.padding(8.dp),
                                    color = Color(0xFFffffff),
                                    text = "$quantity",
                                    fontSize = 20.sp)
                                Button(
                                    colors = buttonColors(containerColor = Color(0xFFEF8121)),
                                    onClick = { quantity++ }
                                ) {
                                    Text("+", fontSize = 20.sp)
                                }
                            }
                        }
                        Button(
                            colors = buttonColors(containerColor = Color(0xFFEF8121)),
                            onClick = {  }
                        ) {
                            Text("Buy", fontSize = 20.sp)
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
                    Text(
                        fontFamily = MaterialTheme.typography.h2.fontFamily,
                        color = Color(0xFF000000),
                        text = "Price: €${String.format("%.2f", product.price)}"
                    )
                }
            }
        }
    } ?: run {
        // handle case where product is null, e.g. loading or error state
    }
}