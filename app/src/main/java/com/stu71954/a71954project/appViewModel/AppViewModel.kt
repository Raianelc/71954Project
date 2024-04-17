package com.stu71954.a71954project.appViewModel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

import com.stu71954.a71954project.appRepository.AppRepository
import com.stu71954.a71954project.model.CartItem
import com.stu71954.a71954project.model.Product
import com.stu71954.a71954project.model.User
import com.stu71954.a71954project.model.UserOrder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.double
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class AppViewModel (private val repository: AppRepository, private val navController: NavController) : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories: StateFlow<List<String>> = _categories

    private val _cartCount = MutableStateFlow<Int>(0)
    val cartCount: StateFlow<Int> = _cartCount

    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> = _product

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    private val _totalAmount = MutableStateFlow<Double>(0.0)
    val totalAmount: StateFlow<Double> = _totalAmount

    private val _cartProducts = MutableStateFlow<List<Product>>(emptyList())
    val cartProducts: StateFlow<List<Product>> = _cartProducts

    private val _orders = MutableStateFlow<List<UserOrder>>(emptyList())
    val orders: StateFlow<List<UserOrder>> = _orders

    private val _orderProducts = MutableStateFlow<List<Product>>(emptyList())
    val orderProducts: StateFlow<List<Product>> = _orderProducts

    private val _orderdt = MutableStateFlow<UserOrder?>(null)

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user


    suspend fun fetchProductsByIdForOrders(productIds: List<Int>): List<Product> {
        return productIds.mapNotNull { id ->
            repository.fetchProductByIdFromApi(id)
        }
    }


    fun getProduct(id: Int) {
        viewModelScope.launch {
            _product.value = products.value.find { it.id == id }
        }
    }

    fun placeOrder(userId: String) {
        val userRef = db.collection("users").document(userId)
        userRef.get().addOnSuccessListener { document ->
            val cart = document.get("cart") as Map<*, *>
            val items = cart["items"] as List<Map<String, Any>>
            val total = cart["total"] as Double

            // Check if the cart is empty
            if (items.isEmpty()) {
                return@addOnSuccessListener
            }

            // Create a new order
            val order = hashMapOf(
                "userId" to userId,
                "items" to items,
                "total" to total
            )

            // Add a new document with a generated ID
            db.collection("orders").add(order).addOnSuccessListener {
                // Clear the user's cart
                userRef.update(
                    "cart.items", emptyList<Map<String, Any>>(),
                    "cart.total", 0.0
                ).addOnSuccessListener {
                    _cartProducts.value = emptyList()
                    _totalAmount.value = 0.0
                    _cartCount.value = 0

                    // Navigate back to ProductListScreen
                    navController.navigate("productlistscreen")
                }
            }
        }
    }

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

    fun fetchCartProducts(userId: String) {
        val userRef = db.collection("users").document(userId)
        userRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val cart = document.get("cart") as Map<*, *>
                    val items = cart["items"] as List<Map<String, Any>>
                    val productIds =
                        items.map { it["id"] as Long }.map { it.toInt() } // Convert to Int
                    fetchProductsByIds(productIds)
                }
            }
    }

    private fun fetchProductsByIds(ids: List<Int>) {
        viewModelScope.launch {
            val products = ids.mapNotNull { id ->
                repository.fetchProductByIdFromApi(id)
            }
            _cartProducts.value = products
            _totalAmount.value = products.sumByDouble { it.price }
        }
    }

    fun removeFirstProductFromCart(userId: String, product: Product) {
        val userRef = db.collection("users").document(userId)
        userRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val cart = document.get("cart") as Map<*, *>
                    val items = cart["items"] as List<Map<String, Any>>
                    val itemIndex = items.indexOfFirst { (it["id"] as Long).toInt() == product.id }
                    if (itemIndex != -1) {
                        // Product is in the cart, remove the first occurrence
                        val newItems = items.toMutableList()
                        newItems.removeAt(itemIndex)
                        userRef.update(
                            "cart.items", newItems,
                            "cart.total", FieldValue.increment(-product.price)
                        ).addOnSuccessListener {
                            getCartCount(userId)
                            fetchCartProducts(userId) // Fetch the updated cart products
                        }
                    }
                }
            }
    }

    fun fetchUserOrders(userId: String) {
        db.collection("orders")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { documents ->
                val orders = documents.map { document ->
                    UserOrder(
                        id = document.id,
                        userId = document.get("userId") as String,
                        items = document.get("items") as List<Map<String, Any>>,
                        total = document.get("total") as Double
                    )
                }
                _orders.value = orders
            }
    }

    fun getOrderProducts(orderId: String) {
        val orderRef = db.collection("orders").document(orderId)

        orderRef.get().addOnSuccessListener { document ->
            val items = document.get("items") as? List<*>

            val productIds =
                items?.mapNotNull { ((it as? Map<String, Any>)?.get("id") as? Long)?.toInt() }

            if (productIds != null) {
                viewModelScope.launch {
                    val products = fetchProductsByIdForOrders(productIds)
                    _orderProducts.value = products
                }
            }
        }
    }


    fun getUserById(userId: String) {
        // Implement the method to fetch user details by userId
        // For example, if you are using Firebase Firestore, you can do something like this:
        db.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val user = User(
                        picture = document.getString("profilePictureUrl") ?: "",
                        firstName = document.getString("firstName") ?: "",
                        lastName = document.getString("lastName") ?: "",
                        email = document.getString("email") ?: "",
                        phone = document.getString("phoneNumber") ?: "",
                        address = document.getString("address") ?: ""
                        // Add other fields as necessary
                    )
                    _user.value = user
                }
            }


    }
}