package com.stu71954.a71954project.appRepository

import com.stu71954.a71954project.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.double
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.OkHttpClient
import okhttp3.Request

class AppRepository {
    suspend fun fetchProductsFromApi(): JsonArray {
        return withContext(Dispatchers.IO) {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("https://fakestoreapi.com/products")
                .build()

            client.newCall(request).execute().use { response ->
                val bodyString = response.body?.string()
                return@withContext Json.parseToJsonElement(bodyString ?: "").jsonArray
            }
        }
    }

    suspend fun fetchProductsByCategoryFromApi(category: String): JsonArray {
        return withContext(Dispatchers.IO) {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("https://fakestoreapi.com/products/category/$category")
                .build()

            client.newCall(request).execute().use { response ->
                val bodyString = response.body?.string()
                return@withContext Json.parseToJsonElement(bodyString ?: "").jsonArray
            }
        }
    }

    suspend fun fetchCategoriesFromApi(): List<String> {
        return withContext(Dispatchers.IO) {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("https://fakestoreapi.com/products/categories")
                .build()

            client.newCall(request).execute().use { response ->
                val bodyString = response.body?.string()
                return@withContext Json.decodeFromString<List<String>>(bodyString ?: "")
            }
        }
    }

    suspend fun fetchProductByIdFromApi(id: Int): Product? {
        return withContext(Dispatchers.IO) {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("https://fakestoreapi.com/products/$id")
                .build()

            client.newCall(request).execute().use { response ->
                val bodyString = response.body?.string()
                val jsonElement = Json.parseToJsonElement(bodyString ?: "")
                return@withContext if (jsonElement is JsonObject) {
                    Product(
                        id = jsonElement["id"]!!.jsonPrimitive.int,
                        title = jsonElement["title"]!!.jsonPrimitive.content,
                        price = jsonElement["price"]!!.jsonPrimitive.double,
                        image = jsonElement["image"]!!.jsonPrimitive.content,
                        category = jsonElement["category"]!!.jsonPrimitive.content,
                        description = jsonElement["description"]!!.jsonPrimitive.content
                    )
                } else {
                    null
                }
            }
        }
    }



}