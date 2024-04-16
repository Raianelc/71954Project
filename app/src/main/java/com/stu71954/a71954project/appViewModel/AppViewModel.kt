package com.stu71954.a71954project.appViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.stu71954.a71954project.appRepository.AppRepository
import com.stu71954.a71954project.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.double
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class AppViewModel (private val repository: AppRepository, private val navController: NavController) : ViewModel(){

    private val db = FirebaseFirestore.getInstance()
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories: StateFlow<List<String>> = _categories

    private val _cartCount = MutableStateFlow<Int>(0)
    val cartCount: StateFlow<Int> = _cartCount

    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> = _product

    fun getProduct(id: Int) {
        viewModelScope.launch {
            _product.value = products.value.find { it.id == id } }}

    init {
        getProducts()
        fetchCategories()
    }

    fun getProducts() {
        viewModelScope.launch {
            val jsonArray = repository.fetchProductsFromApi()
            _products.value = jsonArray.map { jsonElement ->
                val jsonObject = jsonElement.jsonObject
                Product(
                    id = jsonObject["id"]!!.jsonPrimitive.int,
                    title = jsonObject["title"]!!.jsonPrimitive.content,
                    price = jsonObject["price"]!!.jsonPrimitive.double,
                    image = jsonObject["image"]!!.jsonPrimitive.content,
                    category = jsonObject["category"]!!.jsonPrimitive.content,
                    description = jsonObject["description"]!!.jsonPrimitive.content
                )
            }
        }
    }

    fun fetchCategories() {
        viewModelScope.launch {
            _categories.value = repository.fetchCategoriesFromApi()
        }
    }

    fun fetchProductsByCategory(category: String) {
        viewModelScope.launch {
            val jsonArray = repository.fetchProductsByCategoryFromApi(category)
            _products.value = jsonArray.map { jsonElement ->
                val jsonObject = jsonElement.jsonObject
                Product(
                    id = jsonObject["id"]!!.jsonPrimitive.int,
                    title = jsonObject["title"]!!.jsonPrimitive.content,
                    price = jsonObject["price"]!!.jsonPrimitive.double,
                    image = jsonObject["image"]!!.jsonPrimitive.content,
                    category = jsonObject["category"]!!.jsonPrimitive.content,
                    description = jsonObject["description"]!!.jsonPrimitive.content
                )
            }
        }
    }

    fun fetchProductById(productId: Int) {
        viewModelScope.launch {
            _product.value = repository.fetchProductByIdFromApi(productId)
        }
    }

    fun addToCart(userId: String, product: Product) {
        val userRef = db.collection("users").document(userId)
        userRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val cart = document.get("cart") as Map<*, *>
                    val items = cart["items"] as List<Map<String, Any>>
                    val itemIndex = items.indexOfFirst { it["id"] == product.id }
                    if (itemIndex == -1) {
                        // Product is not in the cart, add it
                        val newItem = mapOf("id" to product.id, "quantity" to 1)
                        val newItems = items.toMutableList()
                        newItems.add(newItem)
                        userRef.update(
                            "cart.items", newItems,
                            "cart.total", FieldValue.increment(product.price)
                        )
                            .addOnSuccessListener {
                                getCartCount(userId)
                            }
                    } else {
                        // Product is already in the cart, increment the quantity
                        val item = items[itemIndex].toMutableMap()
                        item["quantity"] = (item["quantity"] as Int) + 1
                        val newItems = items.toMutableList()
                        newItems[itemIndex] = item
                        userRef.update(
                            "cart.items", newItems,
                            "cart.total", FieldValue.increment(product.price)
                        )
                    }
                }
            }

    }

    private fun getCartCount(userId: String) {
        db.collection("users").document(userId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val cart = snapshot.get("cart") as Map<*, *>
                    val items = cart["items"] as List<*>
                    _cartCount.value = items.size
                }
            }

    }

}