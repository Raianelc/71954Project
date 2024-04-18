package com.stu71954.a71954project.shopCart

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.stu71954.a71954project.appViewModel.AppViewModel
import com.stu71954.a71954project.model.Product


@OptIn(ExperimentalMaterial3Api::class)
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF154670),
                    titleContentColor = colors.onPrimary
                ),
                title = { Text("Cart") }
            )
        },
        bottomBar = {
            userId?.let {
                val cartCount = viewModel.cartCount.collectAsState()
                BottomAppBar(
                    backgroundColor = Color(0xFF154670),
                    contentColor = contentColorFor(MaterialTheme.colors.primary)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            color = Color(0xFFFFFFFF),
                            text = "Total: €${String.format("%.2f", totalAmount)}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Button(
                            colors = buttonColors(containerColor = Color(0xFFEF8121)),
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
                            navController.navigate("product/${product.id}")
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
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFAFDFF)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                fontFamily = MaterialTheme.typography.h2.fontFamily,
                color = Color(0xFF000000),
                text = product.title, fontSize = 17.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                color = Color(0xFF000000),
                text = "€${String.format("%.2f", product.price)}", fontSize = 16.sp
            )
            Button(
                colors = buttonColors(containerColor = Color(0xFFEF8121)),
                onClick = { onRemove() }
            ) {
                Text(
                    fontSize = 16.sp,
                    text = "Remove from Cart"
                )
            }
        }
    }
}