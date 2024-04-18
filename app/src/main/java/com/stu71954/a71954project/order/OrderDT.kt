package com.stu71954.a71954project.order

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.stu71954.a71954project.appViewModel.AppViewModel
import com.stu71954.a71954project.model.Product


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OrderDT(orderId: String, navController: NavController, viewModel: AppViewModel = viewModel()) {
    LaunchedEffect(orderId) {
        viewModel.getOrderProducts(orderId)
    }

    val orderProducts by viewModel.orderProducts.collectAsState(emptyList())
    val totalAmount = orderProducts.sumOf { it.price }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF154670),
                    titleContentColor = colors.onPrimary
                ),
                title = { Text("Order Details") }
            )
        },
        bottomBar = {
            BottomAppBar(
                backgroundColor = Color(0xFF154670),
                contentColor = contentColorFor(MaterialTheme.colorScheme.primary)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        color = Color(0xFFFFFFFF),
                        text = "Total: €${String.format("%.2f", totalAmount)}",
                        fontSize = 20.sp
                    )
                }
            }
        },
        content = { innerPadding ->
            Surface {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),

                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(5.dp))
                        OrderProductsList(orderProducts, navController)

                }
            }
        }
    )
}

@Composable
fun OrderProductsList(orderProducts: List<Product>, navController: NavController) {
    LazyColumn {
        items(items = orderProducts) { product: Product ->
            ProductCard(product, navController)
        }
    }
}

@Composable
fun ProductCard(product: Product, navController: NavController) {
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
            .clickable { navController.navigate("product/${product.id}") },
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                color = Color(0xFF000000),
                text = product.title, fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                color = Color(0xFF000000),
                text = "€${String.format("%.2f", product.price)}", fontSize = 16.sp
            )
        }
    }
}