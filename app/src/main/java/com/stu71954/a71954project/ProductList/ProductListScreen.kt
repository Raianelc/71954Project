package com.stu71954.a71954project.ProductList

import androidx.compose.runtime.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.stu71954.a71954project.AppViewModel.AppViewModel
import com.stu71954.a71954project.Auth.AuthViewModel
import com.stu71954.a71954project.Model.Product


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(viewModel: AppViewModel, navController: NavController, authViewModel: AuthViewModel) {

    val categories by viewModel.categories.collectAsState()
    val products by viewModel.products.collectAsState()
    //val userId = authViewModel.getCurrentUserId()
    var selectedCategory by remember { mutableStateOf("") }

    // Fetch the categories when the screen is displayed
    LaunchedEffect(Unit) {
        viewModel.fetchCategories()
    }

    LaunchedEffect(selectedCategory) {
        if (selectedCategory.isNotEmpty() && selectedCategory != "Choose category") {
            viewModel.fetchProductsByCategory(selectedCategory)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(

                title = {
                    Spinner(
                        items = categories,
                        selectedItem = selectedCategory,
                        onItemSelected = { selectedCategory = it }
                    )
                },
                actions = {
                    IconButton(onClick = {  }) {
                        Icon(Icons.Filled.ExitToApp, contentDescription = "Logout")
                    }
                }
            )
        },
        bottomBar = {
//            userId?.let {
//                val cartCount = viewModel.cartCount.collectAsState()
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceEvenly
//                ) {
//                    Text(
//                        text = "Cart: ${cartCount.value} items",
//                        fontWeight = FontWeight.Bold,
//                        fontSize = 18.sp
//                    )
//                    Button(onClick = { navController.navigate("orderHistory") }) {
//                        Text("Orders")
//                    }
//                    Button(onClick = { navController.navigate("cartScreen") }) {
//                        Text("Go to Cart")
//                    }
//                }
//            }
        }
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)

        LazyColumn(modifier = modifier) {
            items(products) { product ->
                ProductCard(product, { //selectedProduct ->
//                    userId?.let { viewModel.addToCart(it, selectedProduct) }
                }, navController)
            }
        }
    }
}

@Composable
fun Spinner(items: List<String>, selectedItem: String, onItemSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        Text(if (selectedItem.isEmpty()) "Choose category" else selectedItem, Modifier.clickable { expanded = true })
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            items.forEach { item ->
                DropdownMenuItem(onClick = {
                    expanded = false
                    onItemSelected(item)
                }) {
                    Text(item)
                }
            }
        }
    }
}

@Composable
fun ProductCard(product: Product, onAddToCartClick: (Product) -> Unit, navController: NavController) {
    Card(modifier = Modifier.padding(8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = rememberImagePainter(product.image),
                contentDescription = product.title,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Text(text = product.title, fontSize = 20.sp, modifier = Modifier.clickable { navController.navigate("productDetail/${product.id}") })
            Text(text = product.price.toString(), fontSize = 16.sp, modifier = Modifier.clickable { navController.navigate("productDetail/${product.id}") })
            Button(onClick = { onAddToCartClick(product) }) {
                Text("Add to Cart")
            }
        }
    }
}