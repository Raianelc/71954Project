package com.stu71954.a71954project.shopCart

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarHost
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.stu71954.a71954project.appViewModel.AppViewModel
import com.stu71954.a71954project.model.Product


@Composable
fun CartScreen(viewModel: AppViewModel, navController: NavController) {
    val productsInCart by viewModel.cartProducts.collectAsState()
    val totalAmount by viewModel.totalAmount.collectAsState()

    val userId = FirebaseAuth.getInstance().currentUser?.uid

    if (userId != null) {
        LaunchedEffect(key1 = userId) {
            viewModel.fetchCartProducts(userId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.primary,
                title = { Text("Cart") }
            )
        },
        bottomBar = {
            userId?.let {
                val cartCount = viewModel.cartCount.collectAsState()
                BottomAppBar(
                    backgroundColor = MaterialTheme.colors.primary,
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Total: $totalAmount", color = Color.White)
                        Button(
                            onClick = {
                                viewModel.placeOrder(userId.toString())
                            }
                        ) {
                            Text("Place Order")
                        }
                    }
                }
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            LazyColumn {
                items(productsInCart) { product ->
                    ProductCard(
                        product = product,
                        onClick = {
                            navController.navigate("productDetail/${product.id}")
                        },
                        onRemove = {
                            userId?.let {
                                viewModel.removeFirstProductFromCart(userId, product)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ProductCard(product: Product, onClick: () -> Unit, onRemove: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = 4.dp
    ) {
        Column {
            Text(text = product.title)
            Text(text = product.price.toString())
            Button(onClick = {
                println("Remove button clicked")
                onRemove()
            }) {
                Text("Remove")
            }
        }
    }
}