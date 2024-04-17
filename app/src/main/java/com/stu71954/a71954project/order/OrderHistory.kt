package com.stu71954.a71954project.order

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.stu71954.a71954project.appViewModel.AppViewModel
import com.stu71954.a71954project.model.UserOrder


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistory(viewModel: AppViewModel, userId: String, navController: NavController) {
    val orders by viewModel.orders.collectAsState(emptyList())

    LaunchedEffect(key1 = userId) {
        viewModel.fetchUserOrders(userId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Order History") }
            )
        },
        content = {
            Surface {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(6.dp),

                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(50.dp))
                    LazyColumn {
                        items(orders) { order ->
                            OrderCard(order = order, onClick = {
                                navController.navigate("orderDT/${order.id}")
                            })
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun OrderCard(order: UserOrder, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Order ID: ${order.id}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Total: ${order.total}")
        }
    }
}