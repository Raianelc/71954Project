package com.stu71954.a71954project.productList

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.stu71954.a71954project.appViewModel.AppViewModel
import com.stu71954.a71954project.auth.AuthViewModel
import com.stu71954.a71954project.model.Product


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(viewModel: AppViewModel, navController: NavController, authViewModel: AuthViewModel) {

    val categories by viewModel.categories.collectAsState()
    val products by viewModel.products.collectAsState()
    val userId = authViewModel.getCurrentUserId()
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF154670),
                    titleContentColor = colors.onPrimary
                ),
                title = {
                    Spinner(
                        items = categories,
                        selectedItem = selectedCategory,
                        onItemSelected = { selectedCategory = it }
                    )
                },
                actions = {
                    IconButton(onClick = { navController.navigate("userProfile/${authViewModel.getCurrentUserId()}") }) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "User Details", tint = Color(0xFFFFFFFF))
                    }
                }
            )
        },
        bottomBar = {
            userId?.let {
                val cartCount = viewModel.cartCount.collectAsState()
                BottomAppBar (
                    backgroundColor = Color(0xFF154670),
                    contentColor = contentColorFor(MaterialTheme.colorScheme.primaryContainer)
                ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        color = Color(0xFFFFFFFF),
                        text = "Cart: ${cartCount.value} items",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Button(
                        colors = buttonColors(containerColor = Color(0xFFEF8121)),
                        onClick = { navController.navigate("orderHistory") }) {
                        Text("Orders")
                    }
                    Button(
                        colors = buttonColors(containerColor = Color(0xFFEF8121)),
                        onClick = { navController.navigate("cart") }) {
                        Text("Go to Cart")

                         }
                    }
                }
            }
        }
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)

        LazyColumn(modifier = modifier) {
            items(products) { product ->
                ProductCard(product, { selectedProduct ->
                    userId?.let { viewModel.addToCart(it, selectedProduct) }
                }, navController)
            }
        }
    }
}


@Composable
fun Spinner(items: List<String>, selectedItem: String, onItemSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(if (selectedItem.isEmpty()) "Choose category" else selectedItem, Modifier.clickable { expanded = !expanded })
            Icon(
                imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                contentDescription = null,
                Modifier.clickable { expanded = !expanded }
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ){
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
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFAFDFF)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .padding(8.dp)
            .size(500.dp, 390.dp)
    ){
        Column(modifier = Modifier.padding(16.dp).fillMaxWidth().fillMaxHeight(), horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.SpaceBetween) {
            Image(
                painter = rememberImagePainter(product.image),
                contentDescription = product.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

                Text(
                    fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                    color = Color(0xFF000000),
                    text = product.title, fontSize = 17.sp, modifier = Modifier.clickable { navController.navigate("product/${product.id}") })

                Text(
                    color = Color(0xFF727274),
                    text = "€${product.price.toString()}", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.clickable { navController.navigate("product/${product.id}") })
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
            ){
                OutlinedButton(
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFFFFFFF),
                        containerColor = Color(0xFFFCE6D3)
                    ),
                    border = BorderStroke(1.dp, Color(0xFFEF8121)),
                    shape = RoundedCornerShape(8.dp),
                    onClick = { onAddToCartClick(product) }
                ) {
                    Text(
                        color = Color(0xFFEF8121),
                        fontSize = 16.sp,
                        text = "Add to Cart"
                    )
                }
            }
        }
    }
}

