package com.stu71954.a71954project.AppViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.stu71954.a71954project.AppRepository.AppRepository
import com.stu71954.a71954project.Model.Product
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


}