package com.stu71954.a71954project.AppRepository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.jsonArray
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



}