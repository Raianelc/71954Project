package com.stu71954.a71954project.order

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.stu71954.a71954project.appViewModel.AppViewModel
import com.stu71954.a71954project.model.Product

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OrderDT(orderId: String, viewModel: AppViewModel = viewModel()) {
    LaunchedEffect(orderId) {
        viewModel.getOrderProducts(orderId)
    }

    val orderProducts by viewModel.orderProducts.collectAsState(emptyList())
    val totalAmount = orderProducts.sumOf { it.price }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Order Details") }
            )
        },
        bottomBar = {
            BottomAppBar(
                backgroundColor = MaterialTheme.colors.background,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Total: $totalAmount EUR", color = Color.Black, fontSize = 20.sp)
                }
            }
        },
        content = {
            Surface {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),

                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(5.dp))
                    OrderProductsList(orderProducts)
                }
            }
        }
    )
}

@Composable
fun OrderProductsList(orderProducts: List<Product>) {
    LazyColumn {
        items(items = orderProducts) { product: Product ->
            ProductCard(product)
        }
    }
}

@Composable
fun ProductCard(product: Product) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Product Name: ${product.title}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Product Price: ${product.price}")
        }
    }
}